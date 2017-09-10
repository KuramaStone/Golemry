package me.xthegamercodes.Golemry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.GolemType;
import me.xthegamercodes.Golemry.golems.type.BreederGolem;
import me.xthegamercodes.Golemry.golems.type.GuardGolem;
import me.xthegamercodes.Golemry.golems.type.HarvestGolem;
import me.xthegamercodes.Golemry.golems.type.MinerGolem;
import me.xthegamercodes.Golemry.golems.type.SeekerGolem;
import me.xthegamercodes.Golemry.golems.utils.GolemUtils;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.Vec3D;

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
		Vec3D spawnedLocation = getSpawnedLocation(golem.split(" ")[1]);

		EntityGolem entitygolem = null;

		if(type == GolemType.BREEDER) {
			entitygolem = new BreederGolem(entity.getWorld());
		}
		else if(type == GolemType.GUARD) {
			entitygolem = new GuardGolem(entity.getWorld());
			entitygolem.setEquipment(0, entity.getEquipment(0));
		}
		else if(type == GolemType.HARVEST) {
			entitygolem = new HarvestGolem(entity.getWorld());
		}
		else if(type == GolemType.MINER) {
			entitygolem = new MinerGolem(entity.getWorld());
			entitygolem.setEquipment(0, entity.getEquipment(0));
		}
		else if(type == GolemType.SEEKER) {
			SeekerGolem seeker = new SeekerGolem(entity.getWorld());
			seeker.inventory.items = GolemUtils.buildItem(entity.toString());
			entitygolem = seeker;
		}
		
		if(entitygolem != null) {
			entitygolem.spawn(currentLocation);
			entitygolem.setSpawnedLocation(spawnedLocation);
			
			entity.getWorld().removeEntity(entity);
		}
	}

	private static Vec3D getSpawnedLocation(String string) {
		string = string.substring(1, string.length()); // 0.0/0.0/0.0
		
		String[] values = string.split("/");

		double a = Double.parseDouble(values[0]);
		double b = Double.parseDouble(values[1]);
		double c = Double.parseDouble(values[2]);
		
		return new Vec3D(a, b, c);
	}

}
