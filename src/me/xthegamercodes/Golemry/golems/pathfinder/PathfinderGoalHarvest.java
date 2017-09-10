package me.xthegamercodes.Golemry.golems.pathfinder;

import me.xthegamercodes.Golemry.golems.type.HarvestGolem;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockCrops;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.InventorySubcontainer;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.World;

public class PathfinderGoalHarvest extends PathfinderGoalGoto {
	
	
	private final HarvestGolem c;
	private boolean d;
	private int f;

	public PathfinderGoalHarvest(HarvestGolem paramEntity, double paramDouble) {
		super(paramEntity, paramDouble, 4);
		this.c = paramEntity;
	}
	
	public boolean a()
	{
		if (this.a <= 0)
		{
			this.f = -1;
			this.d = this.c.cu();
		}
		
		return super.a();
	}
	
	public boolean b()
	{
		return (this.f >= 0) && (super.b());
	}
	
	public void c()
	{
		super.c();
	}
	
	public void d()
	{
		super.d();
	}
	
	public void e()
	{
		super.e();
		
		this.c.getControllerLook().a(this.b.getX() + 0.5D, this.b.getY() + 1, this.b.getZ() + 0.5D, 10.0F, this.c.bQ());
		if (f()) {
			World localWorld = this.c.world;
			BlockPosition localBlockPosition = this.b.up();
			
			IBlockData localIBlockData = localWorld.getType(localBlockPosition);
			Block localBlock = localIBlockData.getBlock();
			if ((this.f == 0) && ((localBlock instanceof BlockCrops)) && (((Integer)localIBlockData.get(BlockCrops.AGE)).intValue() == 7))
			{
			localWorld.setAir(localBlockPosition, true);
			}
			else if ((this.f == 1) && (localBlock == Blocks.AIR)) {
				InventorySubcontainer localInventorySubcontainer = this.c.inventory;
				for (int i = 0; i < localInventorySubcontainer.getSize(); i++)
				{
					ItemStack localItemStack = localInventorySubcontainer.getItem(i);
					int j = 0;
					if (localItemStack != null) {
						if (localItemStack.getItem() == Items.WHEAT_SEEDS)
						{
							localWorld.setTypeAndData(localBlockPosition, Blocks.WHEAT.getBlockData(), 3);
							j = 1;
						}
						else if (localItemStack.getItem() == Items.POTATO)
						{
							localWorld.setTypeAndData(localBlockPosition, Blocks.POTATOES.getBlockData(), 3);
							j = 1;
						}
						else if (localItemStack.getItem() == Items.CARROT)
						{
							localWorld.setTypeAndData(localBlockPosition, Blocks.CARROTS.getBlockData(), 3);
							j = 1;
						}
					}
					if (j != 0)
					{
						localItemStack.count -= 1;
						this.c.updatePickings();
						if(localItemStack.count > 0) {
							break;
						}
						
						localInventorySubcontainer.setItem(i, null);
						break;
					}
				}
			}
			this.f = -1;
			
			this.a = 10;
		}
	}
	
	protected boolean a(World paramWorld, BlockPosition paramBlockPosition)
	{
		Block localBlock = paramWorld.getType(paramBlockPosition).getBlock();
		if (localBlock == Blocks.FARMLAND)
		{
			paramBlockPosition = paramBlockPosition.up();
			IBlockData localIBlockData = paramWorld.getType(paramBlockPosition);
			localBlock = localIBlockData.getBlock();
			if (((localBlock instanceof BlockCrops)) && (((Integer)localIBlockData.get(BlockCrops.AGE)).intValue() == 7) &&
					((this.f == 0) || (this.f < 0)))
			{
				this.f = 0;
				return true;
			}
			if ((localBlock == Blocks.AIR) && (this.d) && ((this.f == 1) || (this.f < 0)))
			{
				this.f = 1;
				return true;
			}
		}
		return false;
	}

}
