package me.xthegamercodes.Golemry.golems.pathfinder;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.utils.GolemUtils;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.Vec3D;

public class PathfinderGoalStroll extends PathfinderGoal {
	
	private EntityGolem a;
	private double b;
	private double c;
	private double d;
	private double e;
	private int f;
	private boolean g;
	  
	public PathfinderGoalStroll(EntityGolem paramEntityCreature, double paramDouble) {
		this(paramEntityCreature, paramDouble, 120);
	}
	  
	public PathfinderGoalStroll(EntityGolem paramEntityCreature, double paramDouble, int paramInt) {
		this.a = paramEntityCreature;
		this.e = paramDouble;
		this.f = paramInt;
		a(1);
	}
	  
	public boolean a() {
		if (!this.g) 	{
			if (this.a.bh() >= 100) {
				return false;
			}
			if (this.a.bc().nextInt(this.f) != 0) {
				return false;
			}
		}
		Vec3D localVec3D = GolemUtils.randomPosition(this.a, 5);
	    if (localVec3D == null) {
	      return false;
	    }
	    this.b = localVec3D.a;
	    this.c = localVec3D.b;
	    this.d = localVec3D.c;
	    this.g = false;
	    return true;
	}
	
	public boolean b() {
		return !this.a.getNavigation().m();
	}
	  
	public void c() {
		this.a.getNavigation().a(this.b, this.c, this.d, this.e);
	}
	  
	public void f() {
		this.g = true;
	}
	  
	public void setTimeBetweenMovement(int paramInt) {
		this.f = paramInt;
	}
}