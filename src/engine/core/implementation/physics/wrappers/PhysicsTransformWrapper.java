package engine.core.implementation.physics.wrappers;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsData;

/**
 * A transform wrapper for PhysicsData.
 * 
 * @eng.dependencies PhysicsData
 */
public class PhysicsTransformWrapper extends TransformWrapper {
	private PhysicsData m_physics;

	public PhysicsTransformWrapper() {
		super(new Aspect(TypeManager.getType(PhysicsData.class)));
	}

	public PhysicsTransformWrapper(Entity parent) {
		super(parent, new Aspect(TypeManager.getType(PhysicsData.class)));
	}

	@Override
	public Vector getPosition() {
		return m_physics.getPosition();
	}

	@Override
	public Vector getCenter() {
		return m_physics.getCenter();
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
		return new PhysicsTransformWrapper();
	}
}
