package engine.core.implementation.physics.wrappers.physics;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.wrappers.VelocityWrapper;

/**
 * Provides the velocity as defined by PhysicsData.
 * 
 * @eng.dependencies PhysicsData
 */
public class PhysicsVelocityWrapper extends VelocityWrapper {
	private PhysicsData m_physics;

	public PhysicsVelocityWrapper() {
		super(new Aspect(TypeManager.getType(PhysicsData.class)));
	}

	public PhysicsVelocityWrapper(Entity parent) {
		super(parent, new Aspect(TypeManager.getType(PhysicsData.class)));
	}

	@Override
	public Vector getVelocity() {
		return m_physics.getVelocity();
	}

	@Override
	public void loadDependencies() {
		m_physics = (PhysicsData) this.loadDependency(TypeManager.getType(PhysicsData.class));
	}

	@Override
	public Component copy() {
		return new PhysicsVelocityWrapper();
	}
}
