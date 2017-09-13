package me.xthegamercodes.Golemry.golems.type;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.World;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.GolemRank;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalGrowth;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;

public class ArboristGolem extends EntityGolem {
	
	public ArboristGolem(net.minecraft.server.v1_8_R3.World world) {
		this(world.getWorld(), GolemRank.STONE);
	}

	public ArboristGolem(World world, GolemRank rank) {
		super(world, Color.fromRGB(13, 82, 12), ChatColor.DARK_GREEN + "Arborist Golem", rank);
	}

	@Override
	protected void goals() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalGrowth(this, 1.0D, 40));
		this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8f));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityGolem.class, 8f));
		this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, true));
	}

}
