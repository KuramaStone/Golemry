package me.xthegamercodes.Golemry.golems.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.xthegamercodes.Golemry.golems.EntityGolem;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityItem;

public class PathfinderGoalNearestItem<T extends EntityItem> extends PathfinderGoalTargetEntity {

	PathfinderGoalNearestItem<? extends EntityItem> instance;
	protected final Class<T> a;
	private final int g;
	protected final DistanceComparator b;
	protected EntityItem d;
	
	private final TestItem tester;

	public PathfinderGoalNearestItem(EntityGolem entitycreature, boolean flag) {
		this(entitycreature, flag, false);
	}

	public PathfinderGoalNearestItem(EntityGolem entitycreature, boolean flag, boolean flag1) {
		this(entitycreature, 10, flag, flag1, null);
	}

	@SuppressWarnings({"unchecked"})
	public PathfinderGoalNearestItem(EntityGolem entitygolem, int i, boolean flag, boolean flag1, 
			TestItem tester) {
		super(entitygolem, flag, flag1);
		this.instance = this;
		this.a = (Class<T>) EntityItem.class;
		this.g = i;
		this.b = new DistanceComparator(entitygolem);
		this.tester = tester != null ? tester : new TestItem() {
			
			@Override
			public boolean testItem(EntityItem item) {
				return true;
			}
		};
		
		a(1);
	}

	public boolean a() {
		if((this.g > 0) && (this.e.bc().nextInt(this.g) != 0)) {
			return false;
		}
		double d0 = f();
		List<T> list = this.e.world.a(this.a, this.e.getBoundingBox().grow(d0, 4.0D, d0));

		Collections.sort(list, this.b);

		for(Entity ent : new ArrayList<>(list)) {
			if(!(ent instanceof EntityItem)) {
				list.remove(ent);
			}
			else {
				EntityItem item = (EntityItem) ent;
				if(!tester.testItem(item)) {
					list.remove(ent);
				}
			}
		}

		if(list.isEmpty()) {
			return false;
		}

		this.d = ((EntityItem) list.get(0));
		c();
		return true;
	}

	public void c() {
		this.e.getNavigation().a(this.d, 1.1D);
		super.c();
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

	public static abstract class TestItem {
		public abstract boolean testItem(EntityItem item);
	}
}
