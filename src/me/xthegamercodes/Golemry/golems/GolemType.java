package me.xthegamercodes.Golemry.golems;

public enum GolemType {

	BREEDER(1), GUARD(2), HARVESTER(3), MINER(4), SEEKER(5), SMITHY(6);

	private int id;

	GolemType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static GolemType getByID(int i) {
		for(GolemType type : GolemType.values()) {
			if(type.getId() == i) {
				return type;
			}
		}
		
		return null;
	}

}
