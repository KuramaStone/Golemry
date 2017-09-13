package me.xthegamercodes.Golemry.golems;

import org.bukkit.Material;

import me.xthegamercodes.Golemry.Golemry;
import me.xthegamercodes.Golemry.golems.type.*;

public enum GolemType {

	BREEDER(1, BreederGolem.class, "BreederGolem", Material.RED_ROSE),
	GUARD(2, GuardGolem.class, "GuardGolem", Material.DIAMOND_SWORD),
	HARVESTER(3, HarvestGolem.class, "HarvesterGolem", Material.DIAMOND_HOE),
	MINER(4, MinerGolem.class, "MinerGolem", Material.DIAMOND_PICKAXE),
	SEEKER(5, SeekerGolem.class, "SeekerGolem", Material.EYE_OF_ENDER),
	SMITHY(6, SmithGolem.class, "SmithGolem", Material.COAL, 1),
	LUMBER(7, LumberGolem.class, "LumberGolem", Material.DIAMOND_AXE),
	ARBORIST(8, ArboristGolem.class, "ArborGolem", Material.LONG_GRASS);

	private int id;
	private Class<? extends EntityGolem> clazz;
	private String entityname;
	
	private Material summoner;
	private short damage;

	GolemType(int id, Class<? extends EntityGolem> clazz, String name, Material summoner) {
		this(id, clazz, name, summoner, 0);
	}
	
	GolemType(int id, Class<? extends EntityGolem> clazz, String name, Material summoner, int damage) {
		this.id = id;
		this.clazz = clazz;
		this.entityname = name;
		this.summoner = summoner;
		this.damage = (short) damage;
		
		Golemry.registerGolem(getGolemClass(), getEntityName());
	}
	

	public int getId() {
		return id;
	}
	
	public Class<? extends EntityGolem> getGolemClass() {
		return clazz;
	}
	
	public String getEntityName() {
		return entityname;
	}
	
	public Material getSummoningItem() {
		return summoner;
	}
	
	public short getDamage() {
		return damage;
	}
	
	public static GolemType getByName(String name) {
		for(GolemType type : GolemType.values()) {
			if(type.getEntityName().equals(name)) {
				return type;
			}
		}
		
		return null;
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
