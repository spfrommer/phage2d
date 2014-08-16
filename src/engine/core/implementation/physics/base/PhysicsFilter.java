package engine.core.implementation.physics.base;

import org.dyn4j.collision.Filter;

import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.logic.filter.CollisionFilterLogic;

/**
 * Supplied to the physics Body and directly interfaces the physics library and the CollisionFilterLogic.
 */
public class PhysicsFilter implements Filter {
	private Entity m_entity;

	public PhysicsFilter(Entity entity) {
		m_entity = entity;
	}

	public Entity getEntity() {
		return m_entity;
	}

	@Override
	public boolean isAllowed(Filter filter) {
		if (filter instanceof PhysicsFilter) {
			Entity entity = ((PhysicsFilter) filter).getEntity();

			CollisionFilterLogic filter1 = (CollisionFilterLogic) m_entity.getComponent(TypeManager
					.typeOf(CollisionFilterLogic.class));

			CollisionFilterLogic filter2 = (CollisionFilterLogic) entity.getComponent(TypeManager
					.typeOf(CollisionFilterLogic.class));

			if (filter1 == null && filter2 == null) {
				return true;
			}
			if (filter1 == null) {
				return filter2.allowCollision(m_entity);
			}
			if (filter2 == null) {
				return filter1.allowCollision(entity);
			}
			return filter1.allowCollision(entity) && filter2.allowCollision(m_entity);
		}
		return true;
	}
}
