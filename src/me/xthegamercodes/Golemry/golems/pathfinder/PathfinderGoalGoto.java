package me.xthegamercodes.Golemry.golems.pathfinder;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.World;

public abstract class PathfinderGoalGoto extends PathfinderGoal {

	private final EntityGolem c;
	private final double d;
	protected int a;
	private int e;
	private int f;
	protected BlockPosition b = BlockPosition.ZERO;
	private boolean g;
	private int h;
	private int i;
	
	private double range;

	public PathfinderGoalGoto(EntityGolem paramEntityCreature, double paramDouble, int paramInt, double range) {
		this(paramEntityCreature, paramDouble, paramInt, range, 1);
	}

	public PathfinderGoalGoto(EntityGolem paramEntityCreature, double paramDouble, int paramInt, double range, int paramDelay) {
		this.c = paramEntityCreature;
		this.d = paramDouble;
		this.h = paramInt;
		this.range = range;
		this.i = paramDelay;
		a(5);
	}

	public boolean a() {
		if(this.a > 0) {
			this.a--;
			return false;
		}
		this.a = (this.i + this.c.bc().nextInt(this.i));

		return g();
	}

	public boolean b() {
		return (this.e >= -this.f) && (this.e <= 1200) && (a(this.c.world, this.b));
	}

	public void c() {
		this.c.getNavigation().a(this.b.getX() + 0.5D, this.b.getY() + 1, this.b.getZ() + 0.5D, this.d);
		this.e = 0;
		this.f = (this.c.bc().nextInt(this.c.bc().nextInt(1200) + 1200) + 1200);
	}

	public void e() {
		if(this.c.c(this.b.up()) > range) {
			this.g = false;
			this.e += 1;
			if(this.e % 40 == 0) {
				this.c.getNavigation().a(this.b.getX() + 0.5D, this.b.getY() + 1, this.b.getZ() + 0.5D, this.d);
			}
		}
		else {
			this.g = true;
			this.e -= 1;
		}
	}

	protected boolean f() {
		return this.g;
	}

	private boolean g() {
		int i = this.h;
		// int j = 1;
		BlockPosition localBlockPosition1 = new BlockPosition(this.c);
		for(int k = 0; k <= 1; k = k > 0 ? -k : 1 - k) {
			for(int m = 0; m < i; m++) {
				for(int n = 0; n <= m; n = n > 0 ? -n : 1 - n) {
					for(int i1 = (n < m) && (n > -m) ? m : 0; i1 <= m; i1 = i1 > 0 ? -i1 : 1 - i1) {
						BlockPosition localBlockPosition2 = localBlockPosition1.a(n, k - 1, i1);
						if((this.c.e(localBlockPosition2)) && (a(this.c.world, localBlockPosition2))) {
							this.b = localBlockPosition2;
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	protected abstract boolean a(World paramWorld, BlockPosition paramBlockPosition);
}
