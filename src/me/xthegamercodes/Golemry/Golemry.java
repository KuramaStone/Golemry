package me.xthegamercodes.Golemry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.GolemType;
import me.xthegamercodes.Golemry.golems.utils.Configuration;
import me.xthegamercodes.Golemry.golems.utils.GolemUtils;
import me.xthegamercodes.Golemry.listener.GolemCreationListener;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.EntityZombie;

public class Golemry extends JavaPlugin {

	private static Golemry instance;

	public static String VERSION = "1.0.0";

	private final List<String> golems = new ArrayList<>();

	private static final String link = "http://bit.ly/2fghgPS";
	
	public Map<ArmorStand, EntityGolem> allGolems = Maps.newHashMap();
	
	private BukkitRunnable golemStands = new BukkitRunnable() {
		
		@Override
		public void run() {
			for(ArmorStand stand : Lists.newArrayList(allGolems.keySet())) {
				if(allGolems.get(stand).dead) {
					stand.remove();
					allGolems.remove(stand);
				}
			}
		}
	};

	@Override
	public void onEnable() {
		instance = this;
		VERSION = getDescription().getVersion();
		loadGolems();
		golemStands.runTaskTimer(instance, 40l, 40l);

		createGolemList();

		getServer().getPluginManager().registerEvents(new GolemCreationListener(), this);
	}

	@Override
	public void onDisable() {
		for(World world : Bukkit.getWorlds()) {
			for(org.bukkit.entity.Entity entity : world.getEntities()) {
				if(entity instanceof Zombie) {
					if(EntityGolem.isEntityGolem((EntityZombie) GolemUtils.getNMSEntity(entity))) {
						try {
							saveGolem((EntityGolem) GolemUtils.getNMSEntity(entity));
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		golemStands.cancel();
		
		for(GolemType type : GolemType.values())
			unregisterGolem(type.getGolemClass(), type.getEntityName());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(sender.hasPermission("Golemry.spawn")) {
			List<String> options = new ArrayList<>(golems);
			if(args.length == 1) {
				for(String str : golems) {
					if(!str.toLowerCase().startsWith(args[0].toLowerCase())) {
						options.remove(str);
					}
				}
			}

			return options;
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("spawngolem")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("Golemry.spawn")) {
					if(args.length == 1) {
						args[0] = args[0].substring(0, 1).toUpperCase() + args[0].substring(1, args[0].length()).toLowerCase();
						if(validGolem(args[0])) {
							EntityGolem entitygolem = GolemUtils.createGolem(GolemUtils.getWorld(player.getWorld()),
									GolemType.getByName(args[0] + "Golem"));

							entitygolem.spawn(player.getLocation());
							player.sendMessage(ChatColor.LIGHT_PURPLE + "Golem has been summoned!");
							return true;
						}
					}
					else {
						sender.sendMessage(ChatColor.RED + "Usage: /sg [Harvest, Seeker, Guard, Miner, Breeder]");
					}
				}
				else {
					sender.sendMessage(ChatColor.RED + "You do not have permission to do this!");
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "Only players may use this command.");
			}
		}

		else if(command.getName().equalsIgnoreCase("updategolemry")) {
			if(sender.isOp()) {
				if(VERSION.equalsIgnoreCase("1.0.0")) {
					sender.sendMessage(ChatColor.RED + "You are on v1.0.0! There is no previous version to update!");
				}
				else {
					sender.sendMessage(ChatColor.GREEN + "Golems updating...");
					int entities_updated = GolemryUpdate.updateGolems();
					sender.sendMessage(ChatColor.GREEN +
							(entities_updated + " golems updated! If you enjoy the plugin,"
									+ " then please give it a review and positive rating @ " + link));
				}
				return true;
			}
			else {
				sender.sendMessage(ChatColor.RED + "You do not have permission to do this.");
			}
		}

		else if(command.getName().equalsIgnoreCase("golemhelp")) {
			for(String str : GolemUtils.HELP) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
			}
		}

		return false;
	}

	private boolean validGolem(String string) {
		for(String name : golems) {
			if(name.equals(string)) {
				return true;
			}
		}
		return false;
	}

	private void loadGolems() {
		Configuration cfg = new Configuration("golems_data.yml");
		FileConfiguration config = cfg.getData();

		for(String uid : config.getKeys(false)) {
			try {
				String toString = config.getString(uid + ".string");
				World world = Bukkit.getWorld(UUID.fromString(config.getString(uid + ".world")));

				String[] d = config.getString(uid + ".location").split("/");
				Location loc = new Location(world, Double.parseDouble(d[0]), Double.parseDouble(d[1]), Double.parseDouble(d[2]),
						Float.parseFloat(d[3]), Float.parseFloat(d[4]));

				GolemType type = GolemType.getByID(config.getInt(uid + ".id"));

				EntityGolem golem = GolemUtils.createGolem(GolemUtils.getWorld(world), type);
				golem.spawn(loc);
				golem.inventory.items = GolemUtils.buildItem(toString);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			config.set(uid, null);
		}
		cfg.saveData();
	}

	private void saveGolem(EntityGolem golem) {
		Configuration cfg = new Configuration("golems_data.yml");
		FileConfiguration config = cfg.getData();

		String uid = golem.getUniqueID().toString();
		String toString = golem.toString();
		String location = golem.locX + "/" + golem.locY + "/" + golem.locZ + "/" + golem.yaw + "/" + golem.pitch;

		config.set(uid + ".string", toString);
		config.set(uid + ".world", golem.getWorld().getWorld().getUID().toString());
		config.set(uid + ".location", location);
		
		String nameID = GolemUtils.getGolemName(golem).split(" ")[0];
		Bukkit.broadcastMessage("nameID=" + nameID);
		nameID = nameID.substring(1, nameID.length());
		GolemType type = GolemType.getByID(Integer.valueOf(nameID));
		
		config.set(uid + ".id", type.getId());

		cfg.saveData();
		golem.getBukkitEntity().remove();
		golem.getStand().remove();
	}

	private void createGolemList() {
		for(GolemType type : GolemType.values()) {
			String name = type.getEntityName();
			name = name.replace("Golem", "");
			golems.add(name);
		}
		golems.stream().sorted((a, b) -> a.compareTo(b));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void registerGolem(Class<? extends Entity> customClass, String name) {
		try {
			((Map) getPrivateField(EntityTypes.class, "c")).put(name, customClass);
			((Map) getPrivateField(EntityTypes.class, "d")).put(customClass, name);
			((Map) getPrivateField(EntityTypes.class, "f")).put(customClass, Integer.valueOf(54));
			((Map) getPrivateField(EntityTypes.class, "g")).put(name, Integer.valueOf(54));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public static void unregisterGolem(Class<? extends Entity> customClass, String name) {
		try {
			((Map) getPrivateField(EntityTypes.class, "c")).remove(name, customClass);
			((Map) getPrivateField(EntityTypes.class, "d")).remove(customClass, name);
			((Map) getPrivateField(EntityTypes.class, "f")).remove(customClass, Integer.valueOf(54));
			((Map) getPrivateField(EntityTypes.class, "g")).remove(name, Integer.valueOf(54));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static Object getPrivateField(Class<?> clazz, String method) {
		try {
			Field field = clazz.getDeclaredField(method);
			field.setAccessible(true);
			return field.get(null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Golemry getPlugin() {
		return instance;
	}

}
