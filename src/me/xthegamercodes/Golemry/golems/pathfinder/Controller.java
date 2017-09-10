package me.xthegamercodes.Golemry.golems.pathfinder;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import net.minecraft.server.v1_8_R3.ControllerMove;

public class Controller extends ControllerMove {
	
	private EntityGolem paramEntityGolem;

	public Controller(EntityGolem paramEntityGolem) {
		super(paramEntityGolem);
		this.paramEntityGolem = paramEntityGolem;
	}
	
	@Override
	public void c() {
		super.c();
		
		paramEntityGolem.updateArmourStand();
	}

}
