package engine.core.implementation.physics.wrappers.physics;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.wrappers.RotationWrapper;

/**
 * Provides the rotation as defined by PhysicsData.
 * 
 * @eng.dependencies PhysicsData
 */
public class PhysicsRotationWrapper extends RotationWrapper {
	private PhysicsData m_physics;

	public PhysicsRotationWrapper() {
		super(new Aspect(TypeManager.getType(PhysicsData.class)));
	}

	public PhysicsRotationWrapper(Entity parent) {
		super(parent, new Aspect(TypeManager.getType(PhysicsData.class)));
	}

	@Override
	public double getRotation() {
		return m_physics.getRotation();
	}

	@Override
	public void loadDependencies() {
		m_physics = (PhysicsData) this.loadDependency(TypeManager.getType(PhysicsData.class));
	}

	@Override
	public Component copy() {
		return new PhysicsRotationWrapper();
	}
}
