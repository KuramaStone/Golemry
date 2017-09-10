package me.xthegamercodes.Golemry.golems.pathfinder;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.utils.GolemUtils;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.Vec3D;

public class PathfinderGoalLinger extends PathfinderGoal {
	
	  private EntityGolem a;
	  private double d;
	  
	  private long delayStart;
	  private int wanderRange;
	  
	  public PathfinderGoalLinger(EntityGolem paramEntity, int wanderRange) {
		  this.a = paramEntity;
		  this.wanderRange = wanderRange;
	  }
	  
	  public boolean a() {
		  return true;
	  }
	  
	  public boolean b() {
		  return true;
	  }
	  
	  public void e() {
		  Vec3D a1 = this.a.spawnedLocation;
		  Vec3D a2 = new Vec3D(this.a.locX, this.a.locY, this.a.locZ);
		  double d1 = a1.distanceSquared(a2);
		  d1 = d1*d1;
		  
		  long now = System.currentTimeMillis();
		  if(this.d < d1 && this.delayStart + 10000 <= now) {
			  Vec3D a3 = GolemUtils.randomPosition(this.a, wanderRange);
			  this.a.getNavigation().a(a3.a, a3.b, a3.c, 1.1D);
			  this.delayStart = now;
		  }
	  }
}

