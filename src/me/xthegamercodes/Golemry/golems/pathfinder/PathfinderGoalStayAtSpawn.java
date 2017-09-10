package me.xthegamercodes.Golemry.golems.pathfinder;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.RandomPositionGenerator;
import net.minecraft.server.v1_8_R3.Vec3D;

public class PathfinderGoalStayAtSpawn extends PathfinderGoal {

	private EntityGolem a;
	private double range;
	private boolean move;

	public PathfinderGoalStayAtSpawn(EntityGolem paramEntity, double range, boolean move) {
		this.a = paramEntity;
		this.range = range;
		this.move = false;
	}

	public boolean a() {
		return true;
	}

	public boolean b() {
		return true;
	}

	public void e() {
		if(f(this.a.locX, this.a.locY, this.a.locZ)) {
			if(move) {
				Vec3D pos = RandomPositionGenerator.a(this.a, 3, 5);
				while(f(pos.a, pos.b, pos.c)) {
					pos = RandomPositionGenerator.a(this.a, 3, 5);
				}
				this.a.getNavigation().a(pos.a, pos.b, pos.c, 1.1f);
			}
			else {
				this.a.getNavigation().a(this.a.spawnedLocation.a, this.a.spawnedLocation.b, this.a.spawnedLocation.c,
						1.1f);
			}
		}
	}

	private boolean f(double locX, double locY, double locZ) {
		Vec3D a1 = this.a.spawnedLocation;
		Vec3D a2 = new Vec3D(locX, locY, locZ);
		double d = a1.distanceSquared(a2);
		d = Math.sqrt(d);

		return d > range;
	}
}
