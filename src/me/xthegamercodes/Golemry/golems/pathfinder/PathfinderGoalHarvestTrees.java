package me.xthegamercodes.Golemry.golems.pathfinder;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.utils.GolemUtils;
import me.xthegamercodes.Golemry.golems.utils.TreeBreaker;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.World;

public class PathfinderGoalHarvestTrees extends PathfinderGoalGoto {
	
	private final EntityGolem c;

	public PathfinderGoalHarvestTrees(EntityGolem paramEntityGolem, double paramDouble, int delay) {
		super(paramEntityGolem, paramDouble, 9, 4.0D, delay);
		this.c = paramEntityGolem;
	}
	
	@Override
	public void e() {
		super.e();

		this.c.getControllerLook().a(this.b.getX() + 0.5D, this.b.getY() + 1, this.b.getZ() + 0.5D, 10.0F, this.c.bQ());
		if(f()) {
			TreeBreaker breaker = new TreeBreaker(405);
			breaker.destroyTree(GolemUtils.toLocation(this.c.world.getWorld(), this.b).getBlock());
			plantTree();
		}
	
	}

	private void plantTree() {
		BlockPosition bp = trailToDirt(this.c.world, this.b);
		World world = this.c.getWorld();
		if(world.getType(bp).getBlock() == Blocks.DIRT) {
			bp = bp.up();
			
			for(int i = 0; i < this.c.inventory.getSize(); i++) {
				ItemStack item = this.c.inventory.getItem(i);
				if(item != null) {
					if(CraftMagicNumbers.getMaterial(item.getItem()) == Material.SAPLING) {
						item.count--;
						
						world.setTypeAndData(bp, Blocks.SAPLING.getBlockData(), item.getData());
						
						if(item.count <= 0) {
							this.c.inventory.setItem(i, null);
						}
						break;
					}
				}
			}
		}
	}

	private BlockPosition trailToDirt(World world, BlockPosition bp) {
		int i = 0;
		while(world.getType(bp).getBlock() != Blocks.DIRT) {
			bp = bp.down();
			
			if(i == 3)
				break;
			i++;
		}
		
		return bp;
	}

	@Override
	protected boolean a(World paramWorld, BlockPosition paramBlockPosition) {
		if(!isLog(paramWorld, paramBlockPosition)) {
			return false;
		}
		
		boolean a = isLog(paramWorld, paramBlockPosition.up()) && isLog(paramWorld, paramBlockPosition.down());
		boolean b = isLog(paramWorld, paramBlockPosition.up()) && isLog(paramWorld, paramBlockPosition.up().up());
		boolean c = isLog(paramWorld, paramBlockPosition.down()) && isLog(paramWorld, paramBlockPosition.down().down());

		return a || b || c;
	}

	private boolean isLog(World paramWorld, BlockPosition paramBlockPosition) {
		Block localBlock = paramWorld.getType(paramBlockPosition).getBlock();
		return localBlock == Blocks.LOG;
	}

}
