package me.xthegamercodes.Golemry.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.BlockStateListPopulator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.GolemType;
import me.xthegamercodes.Golemry.golems.utils.GolemUtils;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.BlockStatePredicate;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.ShapeDetector;
import net.minecraft.server.v1_8_R3.ShapeDetectorBlock;
import net.minecraft.server.v1_8_R3.ShapeDetectorBuilder;
import net.minecraft.server.v1_8_R3.World;

public class GolemCreationListener implements Listener {

	private ShapeDetector harvestGolem;

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onItemFramePlace(PlayerInteractAtEntityEvent event) {
		Player player = event.getPlayer();
		Entity clicked = event.getRightClicked();

		if(clicked instanceof ItemFrame) {
			ItemFrame frame = (ItemFrame) clicked;

			World world = ((CraftWorld) frame.getWorld()).getHandle();
			Block block = frame.getLocation().getBlock();
			block = block.getRelative(frame.getAttachedFace());

			BlockPosition blockposition = new BlockPosition(block.getX(), block.getY(), block.getZ());

			EntityGolem entitygolem = null;

			if(frame.getItem() != null) {
				Material material = frame.getItem().getType();
				GolemType type = GolemUtils.getGolemType(material, frame.getItem().getDurability());

				if(GolemUtils.canCreate(player, type)) {
					entitygolem = GolemUtils.createGolem(GolemUtils.getWorld(block.getWorld()), type);

					if(entitygolem != null) {
						if(detector(entitygolem, world, blockposition)) {
							frame.remove();
						}
					}
				}
			}
		}
	}

	private boolean detector(EntityGolem entitygolem, World world, BlockPosition blockposition) {
		ShapeDetector.ShapeDetectorCollection shapedetectorcollection;

		if((shapedetectorcollection = getGolemDetector().a(world, blockposition)) != null) {
			BlockStateListPopulator blockList = new BlockStateListPopulator(world.getWorld());

			for(int i = 0; i < getGolemDetector().c(); i++) {
				for(int k = 0; k < getGolemDetector().b(); k++) {
					BlockPosition pos = shapedetectorcollection.a(i, k, 0).getPosition();
					blockList.setTypeId(pos.getX(), pos.getY(), pos.getZ(), 0);
				}
			}

			BlockPosition blockposition2 = shapedetectorcollection.a(1, 2, 0).getPosition();

			Location spawn = new Location(world.getWorld(), blockposition2.getX() + 0.5D, blockposition2.getY(),
					blockposition2.getZ() + 0.5D);

			if(entitygolem.spawn(spawn)) {
				blockList.updateList();
				for(int j = 0; j < 120; j++) {
					world.addParticle(EnumParticle.MOB_APPEARANCE, blockposition2.getX() + world.random.nextDouble(),
							blockposition2.getY() + world.random.nextDouble() * 3.9D,
							blockposition2.getZ() + world.random.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
				}
				for(int j = 0; j < getGolemDetector().c(); j++) {
					for(int l = 0; l < getGolemDetector().b(); l++) {
						ShapeDetectorBlock shapedetectorblock2 = shapedetectorcollection.a(j, l, 0);

						world.update(shapedetectorblock2.getPosition(), Blocks.AIR);
					}
				}

				return true;
			}
		}

		return false;
	}

	private ShapeDetector getGolemDetector() {
		if(this.harvestGolem == null) {
			this.harvestGolem = ShapeDetectorBuilder.a().a(new String[] { "~~~", "~#~", "~#~" })
					.a('#', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.HAY_BLOCK)))
					.a('~', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.AIR)))
					.b();
		}

		return this.harvestGolem;
	}

}
