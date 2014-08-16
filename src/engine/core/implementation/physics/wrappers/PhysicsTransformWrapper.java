package engine.core.implementation.physics.wrappers;

import utils.physics.Vector;
import engine.core.framework.Aspect;
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
		super(new Aspect(TypeManager.typeOf(PhysicsData.class)));
	}

	@Override
	public Vector getPosition() {
		return m_physics.getPosition();
	}

	@Override
	public void setPosition(Vector position) {
		m_physics.setPosition(position);
	}

	@Override
	public Vector getCenter() {
		return m_physics.getCenter();
	}

	@Override
	public void setCenter(Vector center) {
		m_physics.setCenter(center);
	}

	@Override
	public double getRotation() {
		return m_physics.getRotation();
	}

	@Override
	public void setRotation(double rotation) {
		m_physics.setRotation(rotation);
	}

	@Override
	public void loadDependencies() {
		m_physics = (PhysicsData) this.loadDependency(TypeManager.typeOf(PhysicsData.class));
	}

	@Override
	public Component copy() {
		return new PhysicsTransformWrapper();
	}
}
