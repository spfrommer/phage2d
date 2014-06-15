package examples.tankarena.entities.tank;

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
import engine.core.implementation.physics.wrappers.PositionWrapper;
import engine.core.implementation.physics.wrappers.RotationWrapper;

/**
 * Shoots any projectile with PhysicsData using its Entity's translation and rotation.
 */
public class ShootingLogic extends LogicComponent implements ActionExecutable {
	private EntitySystem m_system;
	private Entity m_projectile;

	private PositionWrapper m_position;
	private RotationWrapper m_rotation;
	private double m_gunHeat = 5;
	private double m_firingHeat = 5;

	/**
	 * The projectile to shoot and the system that it should be spawned into.
	 * 
	 * @param system
	 * @param projectile
	 */
	public ShootingLogic(EntitySystem system, Entity projectile) {
		super(new Aspect(TypeManager.getType(PositionWrapper.class), TypeManager.getType(RotationWrapper.class)));
		m_system = system;
		m_projectile = projectile;
	}

	/**
	 * The parent entity as well as the projectile to shoot and the system it should be spawned into.
	 * 
	 * @param entity
	 * @param system
	 * @param projectile
	 */
	public ShootingLogic(Entity entity, EntitySystem system, Entity projectile) {
		super(entity,
				new Aspect(TypeManager.getType(PositionWrapper.class), TypeManager.getType(RotationWrapper.class)));
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
		m_position = (PositionWrapper) this.loadDependency(TypeManager.getType(PositionWrapper.class));
		m_rotation = (RotationWrapper) this.loadDependency(TypeManager.getType(RotationWrapper.class));
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
		Vector directionNormal = Vector.dyn4jNormalVector(m_rotation.getRotation());
		physics.setPosition(m_position.getPosition().add(directionNormal.scalarMultiply(80)));
		physics.setRotation(m_rotation.getRotation());
		physics.setVelocity(directionNormal.scalarMultiply(1000));
		m_system.addEntity(projectile);
		m_gunHeat = m_firingHeat;

		return ExecutionState.SUCCESS;
	}
}
