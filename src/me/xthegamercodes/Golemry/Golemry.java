package me.xthegamercodes.Golemry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.type.BreederGolem;
import me.xthegamercodes.Golemry.golems.type.GuardGolem;
import me.xthegamercodes.Golemry.golems.type.HarvestGolem;
import me.xthegamercodes.Golemry.golems.type.MinerGolem;
import me.xthegamercodes.Golemry.golems.type.SeekerGolem;
import me.xthegamercodes.Golemry.golems.type.SmithGolem;
import me.xthegamercodes.Golemry.golems.utils.GolemUtils;
import me.xthegamercodes.Golemry.listener.GolemCreationListener;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityTypes;

public class Golemry extends JavaPlugin {
	
	private static Golemry instance;

	public static String VERSION = "1.0.0";

	private final List<String> golems = new ArrayList<>(Arrays.asList("Breeder", "Guard", "Harvester", "Miner", "Seeker", "Smithy"));
	
	private static final String link = "http://bit.ly/2fghgPS";
	
	@Override
	public void onEnable() {
		instance = this;
		VERSION = getDescription().getVersion();
		registerGolem(HarvestGolem.class, "HarvestGolem", 54);
		registerGolem(SeekerGolem.class, "SeekerGolem", 54);
		registerGolem(GuardGolem.class, "GuardGolem", 54);
		registerGolem(MinerGolem.class, "MinerGolem", 54);
		registerGolem(BreederGolem.class, "BreederGolem", 54);
		registerGolem(SmithGolem.class, "SmithGolem", 54);

		getServer().getPluginManager().registerEvents(new GolemCreationListener(), this);
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
						EntityGolem entitygolem = null;
						if(args[0].equalsIgnoreCase("harvest")) {
							entitygolem = new HarvestGolem(GolemUtils.getWorld(player.getWorld()));
						}
						else if(args[0].equalsIgnoreCase("seeker")) {
							entitygolem = new SeekerGolem(GolemUtils.getWorld(player.getWorld()));
						}
						else if(args[0].equalsIgnoreCase("guard")) {
							entitygolem = new GuardGolem(GolemUtils.getWorld(player.getWorld()));
						}
						else if(args[0].equalsIgnoreCase("miner")) {
							entitygolem = new MinerGolem(GolemUtils.getWorld(player.getWorld()));
						}
						else if(args[0].equalsIgnoreCase("breeder")) {
							entitygolem = new BreederGolem(GolemUtils.getWorld(player.getWorld()));
						}
						else if(args[0].equalsIgnoreCase("smithy")) {
							entitygolem = new SmithGolem(GolemUtils.getWorld(player.getWorld()));
						}

						entitygolem.spawn(player.getLocation());
						player.sendMessage(ChatColor.LIGHT_PURPLE + "Golem has been summoned!");
						return true;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void registerGolem(Class<? extends Entity> customClass, String name, int id) {
		try {
			((Map) getPrivateField(EntityTypes.class, "c")).put(name, customClass);
			((Map) getPrivateField(EntityTypes.class, "d")).put(customClass, name);
			((Map) getPrivateField(EntityTypes.class, "f")).put(customClass, Integer.valueOf(id));
			((Map) getPrivateField(EntityTypes.class, "g")).put(name, Integer.valueOf(id));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private Object getPrivateField(Class<?> clazz, String method) {
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
