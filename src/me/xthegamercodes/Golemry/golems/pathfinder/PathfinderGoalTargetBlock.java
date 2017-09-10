package me.xthegamercodes.Golemry.golems.pathfinder;

import me.xthegamercodes.Golemry.golems.type.MinerGolem;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.World;

public abstract class PathfinderGoalTargetBlock extends PathfinderGoal {

	protected BlockPosition b;
	protected MinerGolem c;
	private int h;
	private boolean g;

	public PathfinderGoalTargetBlock(MinerGolem c, int h) {
		this.c = c;
		this.h = h;
	}

	@Override
	public boolean a() {
		return this.g = this.g();
	}

	public boolean f() {
		return this.g;
	}

	private boolean g() {
		int i = this.h;
		// int j = 1;
		BlockPosition localBlockPosition1 = new BlockPosition(this.c).up();
		for(int k = 0; k <= 1; k = k > 0 ? -k : 1 - k) {
			for(int m = 0; m < i; m++) {
				for(int n = 0; n <= m; n = n > 0 ? -n : 1 - n) {
					for(int i1 = (n < m) && (n > -m) ? m : 0; i1 <= m; i1 = i1 > 0 ? -i1 : 1 - i1) {
						BlockPosition localBlockPosition2 = localBlockPosition1.a(n, k - 1, i1);
						if((this.c.e(localBlockPosition2)) && (a(this.c.world, localBlockPosition2))) {
							this.b = localBlockPosition2;
							// Bukkit.broadcastMessage(this.c.world.getType(localBlockPosition2).getBlock().getName());
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
