package me.xthegamercodes.Golemry.golems.type;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.GolemRank;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalLinger;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalNearestItem;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalStayAtSpawn;
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
		this.goalSelector.a(2, new PathfinderGoalStayAtSpawn(this, 16, true));
		this.goalSelector.a(3, new PathfinderGoalTargetChest(this, 1.0D));
		this.goalSelector.a(4, new PathfinderGoalLinger(this, 5));
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
