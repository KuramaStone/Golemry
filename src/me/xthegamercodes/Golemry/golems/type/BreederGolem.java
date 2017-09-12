package me.xthegamercodes.Golemry.golems.type;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.GolemRank;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalNearestBreedableMob;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalStayAtSpawn;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.World;

public class BreederGolem extends EntityGolem {

	public BreederGolem(World world) {
		this(world, GolemRank.STONE);
	}

	public BreederGolem(World world, GolemRank rank) {
		super(world.getWorld(), Color.FUCHSIA, ChatColor.LIGHT_PURPLE + "Breeder Golem", rank);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void goals() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalNearestBreedableMob(this, true));
		this.goalSelector.a(2, new PathfinderGoalStayAtSpawn(this, 1, false));
		this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8f));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityGolem.class, 8f));
		this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true));
	}

}
