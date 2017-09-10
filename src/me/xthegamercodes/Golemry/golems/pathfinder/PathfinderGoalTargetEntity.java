package me.xthegamercodes.Golemry.golems.pathfinder;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PathEntity;
import net.minecraft.server.v1_8_R3.PathPoint;
import net.minecraft.server.v1_8_R3.PathfinderGoal;

public abstract class PathfinderGoalTargetEntity extends PathfinderGoal {

	protected final EntityGolem e;
	protected boolean f;
	private boolean a;
	private int b;
	private int c;
	// private int d;

	public PathfinderGoalTargetEntity(EntityGolem paramEntityCreature, boolean paramBoolean1, boolean paramBoolean2) {
		// this(paramEntityCreature, paramBoolean, false);
		this.e = paramEntityCreature;
		this.f = paramBoolean1;
		this.a = paramBoolean2;
	}

	public boolean b() {
		Entity localEntityLiving = this.e.getGoalTarget();
		if(localEntityLiving == null) {
			return false;
		}
		if(!localEntityLiving.isAlive()) {
			return false;
		}
		double d1 = f();
		if(this.e.h(localEntityLiving) > d1 * d1) {
			return false;
		}

		return true;
	}

	protected double f() {
		AttributeInstance localAttributeInstance = this.e.getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
		return localAttributeInstance == null ? 2.0D : localAttributeInstance.getValue();
	}

	public void c() {
		this.b = 0;
		this.c = 0;
		// this.d = 0;
	}

	public void d() {
		this.e.setGoalTarget(null);
	}

	public static boolean a(EntityGolem paramEntity, Entity paramEntityItem, boolean paramBoolean1, boolean paramBoolean2) {
		if(paramEntityItem == null) {
			return false;
		}

		return true;
	}

	protected boolean a(Entity paramEntity, boolean paramBoolean) {
		if(!a(this.e, paramEntity, paramBoolean, this.f)) {
			return false;
		}
		if(!this.e.e(new BlockPosition(paramEntity))) {
			return false;
		}
		if(this.a) {
			if(--this.c <= 0) {
				this.b = 0;
			}
			if(this.b == 0) {
				this.b = (a(paramEntity) ? 1 : 2);
			}
			if(this.b == 2) {
				return false;
			}
		}

		return true;
	}

	private boolean a(Entity paramEntity) {
		this.c = (10 + this.e.bc().nextInt(5));
		PathEntity localPathEntity = this.e.getNavigation().a(paramEntity);
		if(localPathEntity == null) {
			return false;
		}
		PathPoint localPathPoint = localPathEntity.c();
		if(localPathPoint == null) {
			return false;
		}

		int i = localPathPoint.a - MathHelper.floor(paramEntity.locX);
		int j = localPathPoint.c - MathHelper.floor(paramEntity.locZ);

		return i * i + j * j <= 2.25D;
	}
}
