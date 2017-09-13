package me.xthegamercodes.Golemry.golems.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class TreeBreaker {

	private int blockLimit, amount;

	public TreeBreaker(int blockLimit) {
		this.blockLimit = blockLimit;
	}

	public void destroyTree(Block tree) {
		for(BlockFace face : BlockFace.values()) {
			breakTree(tree.getRelative(face));
		}
	}

	private void breakTree(Block tree) {
		if(tree.getType() != Material.LOG && tree.getType() != Material.LEAVES) {
			return;
		}

		if(amount >= blockLimit) {
			return;
		}

		tree.breakNaturally();
		for(BlockFace face : BlockFace.values()) {
			breakTree(tree.getRelative(face));
		}
		amount++;
	}

}
