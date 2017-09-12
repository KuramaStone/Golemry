package me.xthegamercodes.Golemry.golems.pathfinder;

import me.xthegamercodes.Golemry.golems.type.SmithGolem;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockFurnace;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.TileEntityFurnace;
import net.minecraft.server.v1_8_R3.World;

public class PathfinderGoalSmith extends PathfinderGoalGoto {
	
	private SmithGolem smith;

	public PathfinderGoalSmith(SmithGolem paramEntity, double paramDouble) {
		super(paramEntity, paramDouble, 4);
		this.smith = paramEntity;
	}
	
	public void e() {
		super.e();

		this.smith.getControllerLook().a(this.b.getX() + 0.5D, this.b.getY() + 1, this.b.getZ() + 0.5D, 10.0F, this.smith.bQ());
		if(f()) {
			World localWorld = this.smith.world;

			IBlockData localIBlockData = localWorld.getType(this.b);
			Block localBlock = localIBlockData.getBlock();
			/*
			 * When they are in range of a chest.
			 */
			if(localBlock instanceof BlockFurnace) {
				/*
				 * [0] = source
				 * [1] = fuel
				 * [2] = result slot
				 */
				TileEntityFurnace furnace = (TileEntityFurnace) localWorld.getTileEntity(this.b);
				
				ItemStack source = this.smith.inventory.getItem(0);
				ItemStack fuel = this.smith.inventory.getItem(1);

				ItemStack f_source = furnace.getItem(0);
				ItemStack f_fuel = furnace.getItem(1);
				
				if(f_source != null) {
					if(f_source.doMaterialsMatch(source)) {
						int f_amount = f_source.count;	
						if(f_amount < f_source.getMaxStackSize()) {
							int neededAmount = f_amount - f_source.getMaxStackSize();
							
							if(neededAmount <= source.count) { // 32 <= 33
								source.count -= neededAmount;
								f_source.count += neededAmount;
							}
							else { // 16 <= 15
								f_source.count += source.count;
								this.smith.inventory.setItem(0, null);
							}
							
						}
					}
				}
				else {
					furnace.setItem(0, source);
					this.smith.inventory.setItem(0, null);
				}

				
				if(f_fuel != null) {
					if(f_fuel.doMaterialsMatch(fuel)) {
						int f_amount = f_fuel.count;	
						if(f_amount < f_fuel.getMaxStackSize()) {
							int neededAmount = f_amount - f_fuel.getMaxStackSize();
							
							if(neededAmount <= fuel.count) { // 32 <= 33
								fuel.count -= neededAmount;
								f_fuel.count += neededAmount;
							}
							else { // 16 <= 15
								f_fuel.count += fuel.count;
								this.smith.inventory.setItem(1, null);
							}
							
						}
					}
				}
				else {
					furnace.setItem(1, fuel);
					this.smith.inventory.setItem(1, null);
				}
			}

			this.a = 10;
		}
	}

	@Override
	protected boolean a(World paramWorld, BlockPosition paramBlockPosition) {
		Block localBlock = paramWorld.getType(paramBlockPosition).getBlock();
		/*
		 * This area causes them to navigate towards a chest.
		 */
		if((localBlock == Blocks.FURNACE) || localBlock == Blocks.LIT_FURNACE) {
			if(this.smith.items() > 0) { // If the Seeker has items in it's inventory
				return true;
			}
		}
		return false;
	}

}
