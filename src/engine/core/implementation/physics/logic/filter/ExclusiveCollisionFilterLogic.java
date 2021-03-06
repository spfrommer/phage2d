package engine.core.implementation.physics.logic.filter;

import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsData;

/**
 * Allows collisions with all objects except one.
 */
public class ExclusiveCollisionFilterLogic extends CollisionFilterLogic {
	private PhysicsData m_physics;

	public ExclusiveCollisionFilterLogic(PhysicsData other) {
		super();
		m_physics = other;
	}

	public ExclusiveCollisionFilterLogic(Entity other) {
		super();
		m_physics = (PhysicsData) other.getComponent(TypeManager.typeOf(PhysicsData.class));
	}

	@Override
	public boolean allowCollision(Entity entity) {
		PhysicsData other = (PhysicsData) entity.getComponent(TypeManager.typeOf(PhysicsData.class));
		return (other != m_physics);
	}

	@Override
	public void loadDependencies() {
	}

	@Override
	public Component copy() {
		return new ExclusiveCollisionFilterLogic(m_physics);
	}
}
