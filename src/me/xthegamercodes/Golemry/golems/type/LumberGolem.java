	package me.xthegamercodes.Golemry.golems.type;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.GolemRank;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalHarvestTrees;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.InventorySubcontainer;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;

public class LumberGolem extends EntityGolem {
	
	public LumberGolem(net.minecraft.server.v1_8_R3.World world) {
		this(world.getWorld(), GolemRank.STONE);
	}

	public LumberGolem(World world, GolemRank rank) {
		super(world, Color.fromRGB(137, 32, 0), ChatColor.DARK_GREEN + "Lumberjack Golem", rank);
	}
	
	@Override
	public void init() {
		super.init();

		j(true);
		this.inventory = new InventorySubcontainer("Items", false, 9);
	}
	
	@Override
	protected void a(EntityItem entityitem) {
		ItemStack itemstack = entityitem.getItemStack();
		Item item = itemstack.getItem();

		if(CraftMagicNumbers.getMaterial(entityitem.getItemStack().getItem()) == Material.SAPLING) {
			ItemStack itemstack1 = this.inventory.a(itemstack);
			if(itemstack1 == null) {
				int count = c(item);
				if(count < 64) {
					entityitem.die();
				}
			}
			else {
				itemstack.count = itemstack1.count;
			}
		}

	}
	
	private int c(Item item) {
		int i = 0;
		for(ItemStack itemstack : this.inventory.items) {
			if(itemstack != null) {
				if(itemstack.getItem() == item) {
					i += itemstack.count;
				}
			}
		}

		return i;
	}

	@Override
	protected void goals() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(3, new PathfinderGoalHarvestTrees(this, 1.0D, 100));
		this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8f));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityGolem.class, 8f));
		this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, true));
	}
	
	/*
	 * Lists all entities the entity holds in the following format: id-amount-damage
	 * 
	 */
	@Override
	public String toString() {
		String string = "";
		
		for(int i = 0; i < inventory.getSize(); i++) {
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
