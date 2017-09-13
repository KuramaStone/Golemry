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
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.InventorySubcontainer;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public abstract class EntityGolem extends EntityZombie {

	public InventorySubcontainer inventory;

	private final String golemName;

	private String GOLEM_TYPEV = "Golemry v1.0";

	protected final GolemRank rank;

	private ArmorStand stand;

	protected ItemStack helmet, chest, legs, boots;

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

	private boolean keepStand = true;

	public void updateArmourStand() {
		if(!keepStand) {
			return;
		}
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
				" l" + (0 + "/" + 0 + "/" + 0) +
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
		setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		
		mark();

		setEquipment(1, boots.cloneItemStack());
		setEquipment(2, legs.cloneItemStack());
		setEquipment(3, chest.cloneItemStack());
		setEquipment(4, helmet.cloneItemStack());

		boolean bool = world.addEntity(this);
		
		updateArmourStand();
		Golemry.getPlugin().allGolems.put(stand, this);
		
		return bool;
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
			for(GolemType type : GolemType.values()) {
				if(type.getGolemClass().getSimpleName().equals(this.getClass().getSimpleName())) {
					this.type = type;
					break;
				}
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

	public String getGolemName() {
		return golemName;
	}

	public ArmorStand getStand() {
		return stand;
	}

}
