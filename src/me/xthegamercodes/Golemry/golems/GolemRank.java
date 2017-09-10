package me.xthegamercodes.Golemry.golems;

import org.bukkit.Color;

public enum GolemRank {

	STONE(Color.fromRGB(100, 100, 100)),
	COAL(Color.fromRGB(41, 41, 41)),
	IRON(Color.fromRGB(233, 233, 233)),
	GOLD(Color.fromRGB(198, 188, 58)),
	DIAMOND(Color.fromRGB(73, 186, 193));

	private Color color;

	GolemRank(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}

}
