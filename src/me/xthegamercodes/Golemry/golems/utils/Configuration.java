package me.xthegamercodes.Golemry.golems.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.xthegamercodes.Golemry.Golemry;

public class Configuration {
	private String fileName = "config.yml";
	private JavaPlugin jp;
	File file;
	FileConfiguration fc;

	public Configuration(String fileName) {
		this.jp = Golemry.getPlugin();
		this.fileName = fileName;
	}

	public File getFile() {
		this.file = new File(this.jp.getDataFolder(), this.fileName);
		return this.file;
	}

	public FileConfiguration getData() {
		createData();
		this.fc = YamlConfiguration.loadConfiguration(getFile());
		return this.fc;
	}

	public void saveData() {
		this.file = new File(this.jp.getDataFolder(), this.fileName);
		try {
			this.fc.save(getFile());
		}
		catch(IOException e) {
			e.printStackTrace();
			System.out.println("Attempting to fix error...");
			createData();
			saveData();
		}
	}

	public void createData() {
		if(!getFile().exists()) {
			if(!this.jp.getDataFolder().exists()) {
				this.jp.getDataFolder().mkdirs();
			}
			this.jp.saveResource(this.fileName, false);
		}
	}
}
