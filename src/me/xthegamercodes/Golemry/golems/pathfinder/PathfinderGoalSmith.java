package me.xthegamercodes.Golemry.golems.pathfinder;

import me.xthegamercodes.Golemry.golems.type.SmithGolem;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockFurnace;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.InventorySubcontainer;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.TileEntityFurnace;
import net.minecraft.server.v1_8_R3.World;

public class PathfinderGoalSmith extends PathfinderGoalGoto {

	private final SmithGolem c;
	// private boolean d;
	// private boolean e;

	public PathfinderGoalSmith(SmithGolem paramEntity, double paramDouble) {
		super(paramEntity, paramDouble, 4, 3.0D);
		this.c = paramEntity;
	}

	public boolean a() {
		return super.a();
	}

	public boolean b() {
		return super.b();
	}

	public void e() {
		super.e();

		this.c.getControllerLook().a(this.b.getX() + 0.5D, this.b.getY() + 1, this.b.getZ() + 0.5D, 10.0F, this.c.bQ());
		if(f()) {
			World localWorld = this.c.world;

			IBlockData localIBlockData = localWorld.getType(this.b);
			Block localBlock = localIBlockData.getBlock();
			/*
			 * When they are in range of a chest.
			 */
			if(localBlock instanceof BlockFurnace) {
				TileEntityFurnace furnace = (TileEntityFurnace) localWorld.getTileEntity(this.b);
				InventorySubcontainer inventory = this.c.inventory;

				if(!insertItemToFurnace(furnace, inventory, 0)) {
					insertItemToFurnace(furnace, inventory, 1);
				}

			}

			this.a = 10;
		}
	}

	private boolean insertItemToFurnace(TileEntityFurnace furnace, InventorySubcontainer inventory, int slot) {
		ItemStack inFurnace = furnace.getItem(slot);
		ItemStack inInv = inventory.getItem(slot);

		if(inInv != null) {
			if(inFurnace != null) {
				if(inFurnace.doMaterialsMatch(inInv)) { // Checks if the items are the same
					if(inFurnace.count < inFurnace.getMaxStackSize()) { // If count isn't 64 (or full). 32 < 64
						int amountNeeded = inFurnace.getMaxStackSize() - inFurnace.count; // How much is needed to fill the stack.
																							// 64 - 32 = 32
						if(inInv.count <= amountNeeded) { // 16 <= 32
							inFurnace.count += inInv.count;
							inInv.count = 0;
						}
						else {
							inInv.count -= amountNeeded;
							inFurnace.count += amountNeeded;
						}

						if(inInv.count <= 0) {
							inventory.setItem(slot, null);
						}
						
						return true;

					}
				}
			}
			else {
				furnace.setItem(slot, inInv);
				inventory.setItem(slot, null);
			}
		}
		

		return false;
	}

	protected boolean a(World paramWorld, BlockPosition paramBlockPosition) {
		Block localBlock = paramWorld.getType(paramBlockPosition).getBlock();
		
		if(localBlock instanceof BlockFurnace) { //If block is a furnace
			if(this.c.items() > 0) {
				return doesFurnaceNeedItems(paramBlockPosition);
			}
		}
		return false;
	}

	private boolean doesFurnaceNeedItems(BlockPosition paramBlockPosition) {
		TileEntityFurnace furnace = (TileEntityFurnace) this.c.getWorld().getTileEntity(paramBlockPosition);
		
		return testInsertion(furnace, this.c.inventory, 0) || testInsertion(furnace, this.c.inventory, 1);
	}
	
	/*
	 * Tests if the furnace needs the item in that slot
	 */
	private boolean testInsertion(TileEntityFurnace furnace, InventorySubcontainer inventory, int slot) {
		ItemStack inFurnace = furnace.getItem(slot);
		ItemStack inInv = inventory.getItem(slot);
		
		if(inInv == null) {
			return false;
		}
		
		if(inFurnace == null) {
			return true;
		}

		if(inFurnace.doMaterialsMatch(inInv)) { // Checks if the items are the same
			if(inFurnace.count < inFurnace.getMaxStackSize()) { // If count isn't 64 (or full). 32 < 64
				return true;
			}
		}

		return false;
	}

}
