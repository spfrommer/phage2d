package engine.core.implementation.physics.wrappers.physics;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.wrappers.CenterWrapper;

/**
 * Provides the center as defined by PhysicsData.
 * 
 * @eng.dependencies PhysicsData
 */
public class PhysicsCenterWrapper extends CenterWrapper {
	private PhysicsData m_physics;

	public PhysicsCenterWrapper() {
		super(new Aspect(TypeManager.getType(PhysicsData.class)));
	}

	public PhysicsCenterWrapper(Entity parent) {
		super(parent, new Aspect(TypeManager.getType(PhysicsData.class)));
	}

	@Override
	public Vector getCenter() {
		return m_physics.getCenter();
	}

	@Override
	public void loadDependencies() {
		m_physics = (PhysicsData) this.loadDependency(TypeManager.getType(PhysicsData.class));
	}

	@Override
	public Component copy() {
		return new PhysicsCenterWrapper();
	}
}
