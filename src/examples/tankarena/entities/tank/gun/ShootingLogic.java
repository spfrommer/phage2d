package examples.tankarena.entities.tank.gun;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.Component;
import engine.core.framework.component.LogicComponent;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.leaf.action.executor.ActionExecutable;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.wrappers.TransformWrapper;

/**
 * Shoots any projectile with PhysicsData using its Entity's translation and rotation.
 * 
 * @eng.dependencies TransformWrapper
 */
public class ShootingLogic extends LogicComponent implements ActionExecutable {
	private EntitySystem m_system;
	private Entity m_projectile;

	private TransformWrapper m_transform;
	private double m_gunHeat = 5;
	private double m_firingHeat = 5;

	/**
	 * The projectile to shoot and the system that it should be spawned into.
	 * 
	 * @param system
	 * @param projectile
	 */
	public ShootingLogic(EntitySystem system, Entity projectile) {
		super(new Aspect(TypeManager.getType(TransformWrapper.class)));
		m_system = system;
		m_projectile = projectile;
	}

	/**
	 * Sets the heat generated when the gun is fired. This is decreased every tick until the heat is zero, when it can
	 * fire again.
	 * 
	 * @param firingHeat
	 */
	public void setFiringHeat(double firingHeat) {
		m_firingHeat = firingHeat;
	}

	@Override
	public void loadDependencies() {
		m_transform = (TransformWrapper) this.loadDependency(TypeManager.getType(TransformWrapper.class));
	}

	@Override
	public Component copy() {
		return new ShootingLogic(m_system, m_projectile);
	}

	@Override
	public ExecutionState update(int ticks) {
		if (m_gunHeat > 0) {
			m_gunHeat -= ticks;
			return ExecutionState.FAILURE;
		}

		Entity projectile = m_projectile.copy();
		PhysicsData physics = (PhysicsData) projectile.getComponent(TypeManager.getType(PhysicsData.class));
		Vector directionNormal = Vector.dyn4jNormalVector(m_transform.getRotation());
		physics.setPosition(m_transform.getPosition().add(directionNormal.scalarMultiply(80)));
		physics.setRotation(m_transform.getRotation());
		physics.setVelocity(directionNormal.scalarMultiply(1000));
		m_system.addEntity(projectile);
		m_gunHeat = m_firingHeat;

		return ExecutionState.SUCCESS;
	}
}
