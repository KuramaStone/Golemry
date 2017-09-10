package me.xthegamercodes.Golemry.golems.pathfinder;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import me.xthegamercodes.Golemry.golems.type.MinerGolem;
import me.xthegamercodes.Golemry.golems.utils.GolemUtils;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.World;

public class PathfinderGoalMine extends PathfinderGoalTargetBlock {

	private final List<Block> validBlocks;

	private int timer;
	private int delay, finalDelay;

	public PathfinderGoalMine(MinerGolem paramEntity, List<Block> validBlocks, int radius, int delay) {
		super(paramEntity, radius);
		this.validBlocks = validBlocks;
		this.c = paramEntity;
		this.finalDelay = delay;
	}

	public boolean a() {
		if(timer > 0) {
			timer--;
			return false;
		}
		else {
			updateTool();
			this.timer = (this.delay);
			return super.a();
		}
	}

	public void updateTool() {
		double value = GolemUtils.getScore(this.c.getEquipment(0));
		delay = (int) (finalDelay * value);
	}

	public void e() {
		super.e();

		this.c.getControllerLook().a(this.b.getX() + 0.5D, this.b.getY() + 1, this.b.getZ() + 0.5D, 10.0F, this.c.bQ());

		if(f()) {
			if(this.c.getEquipment(0) != null) {
				breakNaturally(this.b);
			}
			else {
				this.c.world.setAir(this.b);
			}
		}
	}

	private void breakNaturally(BlockPosition blockposition) {
		org.bukkit.block.Block localBlock = GolemUtils.toLocation(this.c.world.getWorld(), blockposition).getBlock();
		Bukkit.broadcastMessage(localBlock.getType().toString() + " " + localBlock.getLocation().getBlockX() + " " + localBlock.getLocation().getBlockY() + " " + localBlock.getLocation().getBlockZ());
		localBlock.breakNaturally(CraftItemStack.asBukkitCopy(this.c.getEquipment(0)));
		Bukkit.broadcastMessage(this.c.getEquipment(0).getName());
	}

	protected boolean a(World paramWorld, BlockPosition paramBlockPosition) {
		Block block = paramWorld.getType(paramBlockPosition).getBlock();
		return (validBlocks != null) && validBlocks.contains(block);
	}

}
