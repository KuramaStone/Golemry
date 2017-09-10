package me.xthegamercodes.Golemry.golems.type;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.event.entity.EntityTargetEvent;

import com.google.common.base.Predicate;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import me.xthegamercodes.Golemry.golems.GolemRank;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalLinger;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderGoalStroll;
import me.xthegamercodes.Golemry.golems.pathfinder.PathfinderRegen;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.IMonster;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemAxe;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.ItemSword;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.World;

public class GuardGolem extends EntityGolem {

	private Long lastItemGrabbed = -1L;

	public GuardGolem(World world) {
		this(world, GolemRank.STONE);
	}

	public GuardGolem(World world, GolemRank rank) {
		super(world.getWorld(), Color.RED, ChatColor.DARK_RED + "Guard Golem", rank);

		j(true);
	}

	@Override
	public void a(EntityItem item) {
		if(isItemValid(item.getItemStack().getItem())) {
			if(canPickupItem()) {
				dropEquipment(true, 0);

				setEquipment(0, item.getItemStack());
				item.die();

				lastItemGrabbed = System.currentTimeMillis();
			}
		}
	}

	private boolean canPickupItem() {
		return (lastItemGrabbed == -1) || (lastItemGrabbed + 10000 < System.currentTimeMillis());
	}

	private boolean isItemValid(Item item) {
		return (item instanceof ItemSword) || (item instanceof ItemAxe);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();

		getAttributeInstance(GenericAttributes.maxHealth).setValue(50.0D);
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
	}

	@Override
	protected void s(Entity entity) {
		if(((entity instanceof IMonster)) && (!(entity instanceof EntityGolem)) && (bc().nextInt(20) == 0)) {
			setGoalTarget((EntityLiving) entity, EntityTargetEvent.TargetReason.COLLISION, true);
		}

		super.s(entity);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void goals() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, true));
		this.goalSelector.a(2, new PathfinderGoalLinger(this, 16));
		this.goalSelector.a(2, new PathfinderGoalStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(1, new PathfinderRegen(this, 40, 1.0f));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, new Class[0]));
		this.targetSelector.a(2,
				new PathfinderGoalNearestGolemTarget(this, EntityInsentient.class, 10, false, true, IMonster.e));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8f));
		this.targetSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityGolem.class, 8f));
		this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, true));
	}

	static class PathfinderGoalNearestGolemTarget<T extends EntityLiving>
			extends PathfinderGoalNearestAttackableTarget<T> {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public PathfinderGoalNearestGolemTarget(final EntityCreature entitycreature, Class<T> oclass, int i,
				boolean flag, boolean flag1, final Predicate<? super T> predicate) {
			super(entitycreature, oclass, i, flag, flag1, predicate);

			this.c = new Predicate() {
				public boolean a(T object) {
					if((predicate != null) && (!predicate.apply(object))) {
						return false;
					}
					if((object instanceof EntityCreeper)) {
						return false;
					}
					if((object instanceof EntityZombie) && (EntityGolem.isEntityGolem((EntityZombie) object))) {
						return false;
					}

					return GuardGolem.PathfinderGoalNearestGolemTarget.this.a(object, false);
				}

				public boolean apply(Object object) {
					return a((T) object);
				}
			};
		}
	}

	/*
	 * Lists the weapon the entity holds in the following format: id-damage
	 * 
	 */
	@Override
	public String toString() {
		ItemStack item = this.getEquipment(0);
		String string = "";
		if(item != null) {
			int itemID = Item.getId(item.getItem());
			int damage = item.getData();
			string = itemID + "-" + 1 + "-" + damage;
		}

		return string;
	}

}
