package me.xthegamercodes.Golemry.golems.type;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.GolemRank;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalLinger;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalNearestItem;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalNearestItem.TestItem;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalSmith;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalStayAtSpawn;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.InventorySubcontainer;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;

public class SmithGolem extends EntityGolem {

	private static final List<Material> validFuel = Arrays.asList(Material.COAL, Material.COAL_BLOCK, Material.BLAZE_ROD);
	private static final List<Material> validSource = Arrays.asList(Material.COBBLESTONE, Material.NETHERRACK);
	
	private static final TestItem tester = new TestItem() {
		
		@Override
		public boolean testItem(EntityItem item) {
			Material m = CraftItemStack.asCraftMirror(item.getItemStack()).getType();
			if(validFuel.contains(m) || validSource.contains(m)) {
				return true;
			}
			return false;
		}
	};
	
	public SmithGolem(net.minecraft.server.v1_8_R3.World world) {
		this(world.getWorld(), GolemRank.STONE);
	}

	public SmithGolem(World world, GolemRank rank) {
		super(world, Color.BLACK, ChatColor.BLACK + "Smith Golem", rank);
	}
	
	@Override
	public boolean a(EntityHuman entityhuman) {
		entityhuman.openContainer(inventory);
		return true;
	}
	
	@Override
	protected void dropDeathLoot(boolean flag, int i) {
		for(ItemStack item : inventory.items) {
			if(item != null) {
				a(item, 0.0f);
			}
		}
	}
	
	/*
	 * Always puts source materials into slot 1
	 * Always puts fuel materials into slot 0
	 */
	@Override
	protected void a(EntityItem entityitem) {
		ItemStack item = entityitem.getItemStack();
		
		if(isSource(item)) {
			ItemStack source = inventory.getItem(0);
			
			if(source != null) {
				if(source.doMaterialsMatch(item)) {
					if(source.count < source.getMaxStackSize()) {
						int neededAmount = source.count - source.getMaxStackSize();
						
						if(neededAmount <= item.count) { // 32 <= 33
							item.count -= neededAmount;
							source.count += neededAmount;
						}
						else { // 16 <= 15
							source.count += item.count;
							entityitem.die();
						}
					}
				}
			}
			else {
				inventory.setItem(0, item);
				entityitem.die();
			}
		}
		else if(isFuel(item )) {
			ItemStack fuel = inventory.getItem(1);
			if(fuel != null) {
				if(fuel.doMaterialsMatch(item)) {
					if(fuel.count < fuel.getMaxStackSize()) {
						int neededAmount = fuel.count - fuel.getMaxStackSize();
						
						if(neededAmount <= item.count) { // 32 <= 33
							item.count -= neededAmount;
							fuel.count += neededAmount;
						}
						else { // 16 <= 15
							fuel.count += item.count;
							entityitem.die();
						}
						
					}
				}
			}
			else {
				inventory.setItem(1, item);
				entityitem.die();
			}
		}
	}
	
	private boolean isSource(ItemStack item) {
		Material m = CraftItemStack.asCraftMirror(item).getType();
		
		if(m.toString().contains("_ORE")) {
			return true;
		}
		
		return validSource.contains(m);
	}

	private boolean isFuel(ItemStack item) {
		return validFuel.contains(CraftItemStack.asCraftMirror(item).getType());
	}

	@Override
	public void init() {
		super.init();
		j(true);
		this.inventory = new InventorySubcontainer("Items", true, 9);
	}

	public int items() {
		int count = 0;
		for(int i = 0; i < this.inventory.getSize(); i++) {
			ItemStack itemstack = this.inventory.getItem(i);
			if(itemstack != null) {
				count += itemstack.count;
			}
		}

		return count;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void goals() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalSmith(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalStayAtSpawn(this, 16, true));
		this.goalSelector.a(4, new PathfinderGoalLinger(this, 5));
		this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new PathfinderGoalNearestItem(this, 10, true, false, tester));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8f));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityGolem.class, 8f));
		this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, true));
	}
	
	@Override
	public String toString() {
		String string = "";
		
		for(int i = 0; i < 2; i++) {
			ItemStack item = inventory.getItem(i);
			if(item != null) {
				int itemID = Item.getId(item.getItem());
				int amount = item.count;
				int damage = item.getData();
				string += itemID + "-" + amount + "-" + damage + " ";
			}
		}
		
		return string;
	}

}
