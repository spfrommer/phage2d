package engine.core.implementation.physics.wrappers.physics;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.wrappers.PositionWrapper;

/**
 * Provides the position as defined by PhysicsData.
 * 
 * @eng.dependencies PhysicsData
 */
public class PhysicsPositionWrapper extends PositionWrapper {
	private PhysicsData m_physics;

	public PhysicsPositionWrapper() {
		super(new Aspect(TypeManager.getType(PhysicsData.class)));
	}

	public PhysicsPositionWrapper(Entity parent) {
		super(parent, new Aspect(TypeManager.getType(PhysicsData.class)));
	}

	@Override
	public Vector getPosition() {
		return m_physics.getPosition();
	}

	@Override
	public void loadDependencies() {
		m_physics = (PhysicsData) this.loadDependency(TypeManager.getType(PhysicsData.class));
	}

	@Override
	public Component copy() {
		return new PhysicsPositionWrapper();
	}
}
