package me.xthegamercodes.Golemry.golems.pathfinder;

import org.bukkit.scheduler.BukkitRunnable;

import me.xthegamercodes.Golemry.Golemry;
import me.xthegamercodes.Golemry.golems.type.SeekerGolem;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockChest;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.InventorySubcontainer;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.TileEntityChest;
import net.minecraft.server.v1_8_R3.World;

public class PathfinderGoalTargetChest extends PathfinderGoalGoto {

	private final SeekerGolem c;
	// private boolean d;
	// private boolean e;

	public PathfinderGoalTargetChest(SeekerGolem paramEntity, double paramDouble, double range) {
		super(paramEntity, paramDouble, 4, range);
		this.c = paramEntity;
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
			if(localBlock instanceof BlockChest) {
				TileEntityChest localChest = (TileEntityChest) localWorld.getTileEntity(this.b);
				InventorySubcontainer inventory = this.c.inventory;

				playChestAnimation(this.b, localChest, true);

				for(int i = 0; i < inventory.getSize(); i++) { // Get all items in Seeker's inventory
					ItemStack itemstack = inventory.getItem(i);

					if(itemstack != null) {
						ItemStack itemstack1 = getFirstItem(localChest, itemstack.getItem()); // Get first item of type in localChest
						if(itemstack1 == null) {
							int firstEmpty = firstEmpty(localChest.getContents()); // Get first empty slot
							if(firstEmpty != -1) {
								localChest.setItem(firstEmpty, itemstack); // Put the itemstack in that slot
								inventory.setItem(i, null); // Set itemstack to null (removing it)
							}
						}
						else {
							int inChest = itemstack1.count;
							int inInv = itemstack.count;

							int total = inChest + inInv;
							int maxStackSize = itemstack.getItem().getMaxStackSize();

							if(total <= maxStackSize) {
								inChest += inInv;
								inInv = 0;
							}
							else {
								int rem = maxStackSize - total; // r = 2
								inChest = maxStackSize;
								inInv = rem;
							}
							itemstack.count = inInv;
							itemstack1.count = inChest;

							if(inInv <= 0) {
								inventory.setItem(i, null);
							}

						}
					}
				}

				startCloseDelay(this.b, localChest);
				
			}

			this.a = 10;
		}
	}

    private void startCloseDelay(BlockPosition position, TileEntityChest localChest) {
		BukkitRunnable br = new BukkitRunnable() {
			
			@Override
			public void run() {
				playChestAnimation(position, localChest, false);
			}
		};
		
		br.runTaskLater(Golemry.getPlugin(), 40l);
	}

	public void playChestAnimation(BlockPosition position, TileEntityChest chest, boolean open) {
        chest.getWorld().playBlockAction(position, chest.w(), 1, open ? 1 : 0);
    }

	private ItemStack getFirstItem(TileEntityChest localChest, Item item) {
		for(int i = 0; i < localChest.getSize(); i++) {
			ItemStack itemstack = localChest.getItem(i);
			if(itemstack != null) {
				if(itemstack.getItem() == item) {
					return itemstack;
				}
			}
		}

		return null;
	}

	private int firstEmpty(ItemStack[] contents) {
		for(int i = 0; i < contents.length; i++) {
			ItemStack itemstack = contents[i];
			if(itemstack == null) {
				return i;
			}
		}
		return -1;
	}

	protected boolean a(World paramWorld, BlockPosition paramBlockPosition) {
		Block localBlock = paramWorld.getType(paramBlockPosition).getBlock();
		/*
		 * This area causes them to navigate towards a chest.
		 */
		if(localBlock == Blocks.CHEST) {
			if(this.c.items() > 0) { // If the Seeker has items in it's inventory
				return true;
			}
		}
		return false;
	}

}
