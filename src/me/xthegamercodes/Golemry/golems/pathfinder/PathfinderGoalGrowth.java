package me.xthegamercodes.Golemry.golems.pathfinder;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.xthegamercodes.Golemry.Golemry;
import me.xthegamercodes.Golemry.golems.EntityGolem;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.EnumColor;
import net.minecraft.server.v1_8_R3.ItemDye;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.World;

public class PathfinderGoalGrowth extends PathfinderGoalGoto {
	
	private ItemStack white_dye = new ItemStack(Items.DYE, EnumColor.WHITE.getColorIndex());
	
	private EntityGolem c;
	
	@Override
	public boolean a() {
		Bukkit.broadcastMessage("1");
		return super.a();
	}

	public PathfinderGoalGrowth(EntityGolem paramEntityCreature, double paramDouble, int delay) {
		super(paramEntityCreature, paramDouble, 15, 3.0D, delay);
		this.c = paramEntityCreature;
	}
	
	@Override
	public void e() {
		super.e();
		Bukkit.broadcastMessage("2:" + this.b.toString());
		
		this.c.getControllerLook().a(this.b.getX() + 0.5D, this.b.getY() + 0.25D, this.b.getZ() + 0.5D, 10.0F, this.c.bQ());
		if(f()) {
			Bukkit.broadcastMessage("3");
			this.c.bf();

			World world = this.c.getWorld();
			this.c.setEquipment(0, white_dye);
			
			ItemDye.a(white_dye, world, this.b);
			world.triggerEffect(2005, this.b, 0);
			changeItem().runTaskLater(Golemry.getPlugin(), 30l);
		}
	}

	private BukkitRunnable changeItem() {
		return new BukkitRunnable() {
			
			@Override
			public void run() {
				c.setEquipment(0, null);
			}
		};
	}

	@Override
	protected boolean a(World paramWorld, BlockPosition paramBlockPosition) {
		Block block = paramWorld.getType(paramBlockPosition).getBlock();
		
		if(block == Blocks.SAPLING) {
			return true;
		}
		
		return false;
	}

}
