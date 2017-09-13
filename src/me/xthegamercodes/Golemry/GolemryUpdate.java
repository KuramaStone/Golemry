package me.xthegamercodes.Golemry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.GolemType;
import me.xthegamercodes.Golemry.golems.utils.GolemUtils;
import net.minecraft.server.v1_8_R3.EntityZombie;

public class GolemryUpdate {

	public static int updateGolems() {
		int entities_updated = 0;
		for(World world : Bukkit.getWorlds()) {
			entities_updated += updateWorld(world);
		}
		
		return entities_updated;
	}

	private static int updateWorld(World world) {
		int entities_updated = 0;
		for(Entity entity : world.getEntities()) {
			if(entity instanceof Zombie) {
				if(EntityGolem.isEntityGolem((EntityZombie) GolemUtils.getNMSEntity(entity))) {
					updateGolem((EntityZombie) GolemUtils.getNMSEntity(entity));
					entities_updated++;
				}
			}
		}
		
		return entities_updated;
	}

	private static void updateGolem(EntityZombie entity) {
		String golem = GolemUtils.getGolemName(entity); // "g1 l0.0/0.0/0.0 v1.0.0
		
		Location currentLocation = entity.getBukkitEntity().getLocation();
		
		String nameID = golem.split(" ")[0];
		
		String G_ = nameID.substring(1, nameID.length());
		int id = Integer.valueOf(G_);
		GolemType type = GolemType.getByID(id);

		EntityGolem entitygolem = GolemUtils.createGolem(entity.world, type);
		if(entitygolem.inventory != null) {
			entitygolem.inventory.items = GolemUtils.buildItem(entity.toString());
		}
		
		entitygolem.setEquipment(0, entity.getEquipment(0));
		
		if(entitygolem != null) {
			entitygolem.spawn(currentLocation);
			
			entity.getWorld().removeEntity(entity);
		}
	}

}
