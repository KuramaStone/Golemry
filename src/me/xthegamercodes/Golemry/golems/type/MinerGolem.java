package me.xthegamercodes.Golemry.golems.type;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.GolemRank;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalMine;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalStayAtSpawn;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemPickaxe;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.Vec3D;
import net.minecraft.server.v1_8_R3.World;

public class MinerGolem extends EntityGolem {

	private List<Block> validBlocks;

	public Long lastItemGrabbed = -1L;

	public MinerGolem(World world) {
		this(world, GolemRank.STONE);
	}

	public MinerGolem(World world, GolemRank rank) {
		super(world.getWorld(), Color.GRAY, ChatColor.GRAY + "Miner Golem", rank);
	}
	
	@Override
	public void init() {
		super.init();
		validBlocks = Arrays.asList(Blocks.STONE, Blocks.GRASS, Blocks.GRAVEL, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.COAL_ORE, Blocks.DIAMOND_ORE, Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.EMERALD_ORE, Blocks.QUARTZ_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.MONSTER_EGG);
		
		j(true);
	}

	@Override
	public boolean spawn(Location location) {
		boolean bool = super.spawn(location);
		spawnedLocation = new Vec3D(location.getBlockX() + 0.5d, location.getY(), location.getBlockZ() + 0.5d);
		mark();
		return bool;
	}

	@Override
	public void a(EntityItem item) {
		if(isItemValid(item.getItemStack().getItem())) {
			if(canPickupItem()) {
				dropEquipment(true, 0);

				setEquipment(0, item.getItemStack());
				item.die();

				lastItemGrabbed = System.currentTimeMillis();
			}
		}
	}

	private boolean canPickupItem() {
		return (lastItemGrabbed == -1) || (lastItemGrabbed + 10000 < System.currentTimeMillis());
	}

	private boolean isItemValid(Item item) {
		return (item instanceof ItemPickaxe);
	}

	@Override
	protected void goals() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalStayAtSpawn(this, 0.25D, false));
		this.goalSelector.a(1, new PathfinderGoalMine(this, validBlocks, 2, 20));
		this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8f));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityGolem.class, 8f));
		this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true));
	}
	
	@Override
	public String toString() {
		ItemStack item = this.getEquipment(0);
		String string = "";
		if(item != null) {
			int itemID = Item.getId(item.getItem());
			int damage = item.getData();
			string = itemID + "-" + 1 + "-" + damage;
		}

		return string;
	}

}
