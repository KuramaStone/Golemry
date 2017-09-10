package me.xthegamercodes.Golemry.golems.type;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.GolemRank;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalHarvest;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalLinger;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalStayAtSpawn;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.InventorySubcontainer;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.World;

public class HarvestGolem extends EntityGolem {

	public InventorySubcontainer inventory;

	private List<Item> pickings;

	public HarvestGolem(World world) {
		this(world, GolemRank.STONE);
	}

	public HarvestGolem(World world, GolemRank rank) {
		super(world.getWorld(), Color.GREEN, ChatColor.GREEN + "Harvest Golem", rank);
	}
	
	@Override
	public void init() {
		super.init();

		this.inventory = new InventorySubcontainer("Items", false, 9);
		this.pickings = new ArrayList<>();
		pickings.add(Items.POTATO);
		pickings.add(Items.CARROT);
		pickings.add(Items.WHEAT_SEEDS);
		j(true);
	}

	public boolean cu() {
		for(int i = 0; i < this.inventory.getSize(); i++) {
			ItemStack itemstack = this.inventory.getItem(i);
			if((itemstack != null) && ((itemstack.getItem() == Items.WHEAT_SEEDS)
					|| (itemstack.getItem() == Items.POTATO) || (itemstack.getItem() == Items.CARROT))) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void a(EntityItem entityitem) {
		ItemStack itemstack = entityitem.getItemStack();
		Item item = itemstack.getItem();

		if(a(item)) {
			ItemStack itemstack1 = this.inventory.a(itemstack);
			if(itemstack1 == null) {
				if(b(item)) {
					int count = c(item);
					if(count < 64) {
						entityitem.die();
					}
				}
				else {
					entityitem.die();
				}

				updatePickings();
			}
			else {
				itemstack.count = itemstack1.count;
			}
		}

	}

	public void updatePickings() {
		int carrotC = c(Items.CARROT);
		int potatoC = c(Items.POTATO);

		if(carrotC > 63) {
			if(pickings.contains(Items.CARROT)) {
				pickings.remove(Items.CARROT);
			}
		}
		else {
			if(!pickings.contains(Items.CARROT)) {
				pickings.add(Items.CARROT);
			}
		}

		if(potatoC > 63) {
			if(pickings.contains(Items.POTATO)) {
				pickings.remove(Items.POTATO);
			}
		}
		else {
			if(!pickings.contains(Items.POTATO)) {
				pickings.add(Items.POTATO);
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

	private boolean a(Item item) {
		return pickings.contains(item);
	}

	private boolean b(Item item) {
		return (item == Items.POTATO) || (item == Items.CARROT);
	}

	@Override
	protected void goals() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalHarvest(this, 1.0D));
		this.goalSelector.a(2, new PathfinderGoalStayAtSpawn(this, 5, true));
		this.goalSelector.a(3, new PathfinderGoalLinger(this, 5));
		// this.goalSelector.a(2, new PathfinderGoalStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8f));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityGolem.class, 8f));
		this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true));
	}

	public InventorySubcontainer getInventory() {
		return inventory;
	}

}
