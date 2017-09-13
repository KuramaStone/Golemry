package me.xthegamercodes.Golemry.golems.type;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;

import com.google.common.collect.Lists;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.GolemRank;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalNearestItem;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalTargetChest;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.InventorySubcontainer;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.World;

public class SeekerGolem extends EntityGolem {
	
	private List<EntityHuman> playersInInv = Lists.newArrayList();

	public SeekerGolem(World world) {
		this(world, GolemRank.STONE);
	}

	public SeekerGolem(World world, GolemRank rank) {
		super(world.getWorld(), Color.YELLOW, ChatColor.YELLOW + "Seeker Golem", rank);
	}
	
	@Override
	public void init() {
		super.init();

		j(true);
		this.inventory = new InventorySubcontainer("Items", false, 9);
	}

	@Override
	public boolean a(EntityHuman entityhuman) {
		entityhuman.openContainer(inventory);
		playersInInv.add(entityhuman);
		Bukkit.broadcastMessage("1");
		return true;
	}

	public void a(EntityItem entityitem) {
		ItemStack itemstack = entityitem.getItemStack();

		ItemStack itemstack1 = this.inventory.a(itemstack);
		if(itemstack1 == null) {
			entityitem.die();
		}
		else {
			itemstack.count = itemstack1.count;
		}
	}
	
	@Override
	public void die() {
		Bukkit.broadcastMessage("2");
		for(EntityHuman human : new ArrayList<>(playersInInv)) {
			if(compare(human.activeContainer.getBukkitView().getTopInventory(), inventory)) {
				human.closeInventory();
				playersInInv.remove(human);
				Bukkit.broadcastMessage("3");
			}
		}
		super.die();
	}

	private boolean compare(Inventory i1, InventorySubcontainer i2) {

		for(int i = 0; i < 9; i++) {
			if(!areItemsSame(CraftItemStack.asNMSCopy(i1.getItem(i)), i2.getItem(i))) {
				return false;
			}
		}
		
		return true;
	}

	private boolean areItemsSame(ItemStack a, ItemStack b) {
		if((a == null) && (b == null)) {
			return true;
		}
		
		if(a != null && b != null) {
			if(a.hasName() && b.hasName()) {
				if(a.getName().equals(b.getName())) {
					if(ItemStack.fastMatches(a, b)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	protected void dropDeathLoot(boolean flag, int i) {
		for(ItemStack item : inventory.items) {
			if(item != null) {
				a(item, 0.0f);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void goals() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalNearestItem(this, true));
		this.goalSelector.a(3, new PathfinderGoalTargetChest(this, 1.0D, 3.0D));
		this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8f));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityGolem.class, 8f));
		this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, true));
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
