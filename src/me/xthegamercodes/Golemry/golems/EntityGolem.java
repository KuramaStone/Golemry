package me.xthegamercodes.Golemry.golems;

import java.lang.reflect.Field;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.xthegamercodes.Golemry.Golemry;
import me.xthegamercodes.Golemry.golems.pathfinder.Controller;
import me.xthegamercodes.Golemry.golems.type.BreederGolem;
import me.xthegamercodes.Golemry.golems.type.GuardGolem;
import me.xthegamercodes.Golemry.golems.type.HarvestGolem;
import me.xthegamercodes.Golemry.golems.type.MinerGolem;
import me.xthegamercodes.Golemry.golems.type.SeekerGolem;
import me.xthegamercodes.Golemry.golems.type.SmithGolem;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.InventorySubcontainer;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.Vec3D;

public abstract class EntityGolem extends EntityZombie {

	public InventorySubcontainer inventory;
	
	private final String golemName;

	private String GOLEM_TYPEV = "Golemry v1.0";

	protected final GolemRank rank;

	private ArmorStand stand;

	protected ItemStack helmet, chest, legs, boots;
	public Vec3D spawnedLocation;

	public EntityGolem(World world, Color color, String customname, GolemRank rank) {
		super(((CraftWorld) world).getHandle());
		this.golemName = customname;
		this.rank = rank;
		this.moveController = new Controller(this);
		helmet = armour(Material.LEATHER_HELMET, color);
		chest = armour(Material.LEATHER_CHESTPLATE, color);
		legs = armour(Material.LEATHER_LEGGINGS, color);
		boots = armour(Material.LEATHER_BOOTS, color);

		init();
		reset();
		goals();
		this.persistent = true;
	}

	public void updateArmourStand() {
		Location loc = new Location(world.getWorld(), locX, locY - 1.0, locZ, yaw, pitch);
		if((stand == null) || (stand.isDead())) {
			stand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
			stand.setCustomName(getGolemName());
			stand.setCustomNameVisible(true);
			stand.setGravity(false);
			stand.setVisible(false);
			return;
		}
		else {
			stand.teleport(loc);
		}
		// Bukkit.broadcastMessage(loc.getBlockX() + " " + loc.getBlockY() + " " +
		// loc.getBlockZ());
	}
	
	@Override
	public void die() {
		super.die();
		stand.remove();
	}

	public void init() {
		getType();
	}

	protected void mark() {
		GOLEM_TYPEV = "g" + type.getId() +
				" l" + (spawnedLocation.a + "/" + spawnedLocation.b + "/" + spawnedLocation.c) +
				" v" + Golemry.VERSION;

		NBTTagCompound nbt = new NBTTagCompound();
		this.b(nbt);
		nbt.setString("GolemryType", GOLEM_TYPEV);
		this.c(nbt);
	}

	@Override
	public void b(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		nbttagcompound.setString("GolemryType", GOLEM_TYPEV);
	}

	public boolean spawn(Location location) {
		spawnedLocation = new Vec3D(location.getX(), location.getY(), location.getZ());
		setPositionRotation(location.getX(), location.getY(), location.getZ(), -90.0f, 0.0f);

		mark();

		setEquipment(1, boots.cloneItemStack());
		setEquipment(2, legs.cloneItemStack());
		setEquipment(3, chest.cloneItemStack());
		setEquipment(4, helmet.cloneItemStack());
		setCustomNameVisible(false);

		return world.addEntity(this);
	}

	protected ItemStack armour(Material leatherArmour, Color color) {
		org.bukkit.inventory.ItemStack itemstack = new org.bukkit.inventory.ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta) itemstack.getItemMeta();
		meta.setColor(color);
		itemstack.setItemMeta(meta);

		return CraftItemStack.asNMSCopy(itemstack);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();

		setBaby(true);
	}

	protected abstract void goals();

	@Override
	protected String bo() {
		return "mob.villager.hit";
	}

	@Override
	protected String bp() {
		return "mob.villager.death";
	}

	// DROPS!!!

	@Override
	protected void dropDeathLoot(boolean flag, int i) {
	}

	@Override
	protected void dropEquipment(boolean flag, int i) {
		if(this.getEquipment(i) != null) {
			a(this.getEquipment(i), 1);
		}
	}

	@Override
	protected void getRareDrop() {
	}

	////////////////////////////

	protected void reset() {
		try {
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private GolemType type;

	public GolemType getType() {
		if(type == null) {
			if(this instanceof BreederGolem) {
				type = GolemType.BREEDER;
			}
			else if(this instanceof GuardGolem) {
				type = GolemType.GUARD;
			}
			else if(this instanceof HarvestGolem) {
				type = GolemType.HARVESTER;
			}
			else if(this instanceof MinerGolem) {
				type = GolemType.MINER;
			}
			else if(this instanceof SeekerGolem) {
				type = GolemType.SEEKER;
			}
			else if(this instanceof SmithGolem) {
				type = GolemType.SMITHY;
			}
		}
		return type;
	}

	public static boolean isEntityGolem(EntityZombie entity) {
		if(entity == null || !entity.isBaby()) {
			return false;
		}
		NBTTagCompound nbt = new NBTTagCompound();
		entity.b(nbt);
		return nbt.hasKey("GolemryType");
	}

	public void setSpawnedLocation(Vec3D spawnedLocation) {
		this.spawnedLocation = spawnedLocation;
	}

	public String getGolemName() {
		return golemName;
	}

}
