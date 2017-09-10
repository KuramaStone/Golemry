package me.xthegamercodes.Golemry.golems.pathfinder;

import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.PathfinderGoal;

public class PathfinderRegen extends PathfinderGoal {

	private final EntityLiving entity;
	private final int delay;
	private final float regenAmount;
	
	private int timer = 0;
	
	public PathfinderRegen(EntityLiving entity, int delay, float regenAmount) {
		this.entity = entity;
		this.delay = delay;
		this.regenAmount = regenAmount;
		
		timer = delay + 0;
	}

	@Override
	public boolean a() {
		if(timer != 0) {
			timer--;
			return false;
		}
		
		timer = delay + 0;
		
		this.entity.heal(regenAmount, RegainReason.REGEN);
		
		return true;
	}

}