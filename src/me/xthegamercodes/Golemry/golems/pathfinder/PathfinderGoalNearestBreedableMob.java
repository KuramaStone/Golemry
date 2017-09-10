package me.xthegamercodes.Golemry.golems.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityAnimal;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.IEntitySelector;

public class PathfinderGoalNearestBreedableMob<T extends EntityAnimal> extends PathfinderGoalTargetEntity {

	PathfinderGoalNearestBreedableMob<? extends EntityAnimal> instance;
	protected final Class<T> a;
	private final int g;
	protected final DistanceComparator b;
	protected Predicate<? super T> c;
	protected EntityAnimal d;

	private int h;

	public PathfinderGoalNearestBreedableMob(EntityGolem entitycreature, boolean flag) {
		this(entitycreature, flag, false);
	}

	public PathfinderGoalNearestBreedableMob(EntityGolem entitycreature, boolean flag, boolean flag1) {
		this(entitycreature, 4, flag, flag1, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PathfinderGoalNearestBreedableMob(EntityGolem entitycreature, int i, boolean flag, boolean flag1, final Predicate<? super T> predicate) {
		super(entitycreature, flag, flag1);
		this.instance = this;
		this.a = (Class<T>) EntityAnimal.class;
		this.g = i;
		this.b = new DistanceComparator(entitycreature);
		a(1);
		this.c = new Predicate() {
			public boolean a(T t0) {
				if((predicate != null) && (!predicate.apply(t0))) {
					return false;
				}

				return instance.a(t0, false);
			}

			public boolean apply(Object object) {
				return a((T) object);
			}
		};
	}

	public boolean a() {
		if(this.h > 0) {
			this.h--;
			return false;
		}
		this.h = 25 + this.e.bc().nextInt(25);
		
		if(this.e.bc().nextInt(this.g) != 0) {
			return false;
		}

		double d0 = f();
		List<T> list = this.e.world.a(this.a, this.e.getBoundingBox().grow(d0, 4.0D, d0), Predicates.and(this.c, IEntitySelector.d));

		Collections.sort(list, this.b);

		for(Entity ent : new ArrayList<>(list)) {
			if(ent == this.d) {
				list.remove(ent);
			}
			
			if(!(ent instanceof EntityAnimal)) {
				list.remove(ent);
			}
			else {
				EntityAnimal animal = (EntityAnimal) ent;
				if(animal.isBaby() || animal.isInLove()) {
					list.remove(ent);
				}
			}
		}

		if(list.isEmpty()) {
			return false;
		}

		this.d = ((EntityAnimal) list.get(0));
		c();
		return true;
	}

	public void c() {
		this.e.getControllerLook().a(this.d.locX, this.d.locY + this.d.getHeadHeight(), this.d.locZ, 10.0F, this.d.bQ());
		setBreeding();
		super.c();
	}

	private void setBreeding() {
		this.d.c((EntityHuman) null);
	}

	public static class DistanceComparator implements Comparator<Entity> {
		private final Entity a;

		public DistanceComparator(Entity entity) {
			this.a = entity;
		}

		public int a(Entity entity, Entity entity1) {
			double d0 = this.a.h(entity);
			double d1 = this.a.h(entity1);

			return d0 > d1 ? 1 : d0 < d1 ? -1 : 0;
		}

		public int compare(Entity object, Entity object1) {
			return a(object, object1);
		}
	}
}
