package engine.core.implementation.spawning.logic;

import java.util.ArrayList;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.wrappers.PositionWrapper;
import engine.core.implementation.physics.wrappers.RotationWrapper;
import engine.inputs.InputManager;

/**
 * Spawns a bullet entity at a certain position and rotation.
 * 
 * @eng.dependencies PositionWrapper, RotationWrapper
 */
public class GunLogic extends SpawningLogic {
	private Entity m_spawningEntity;
	private InputManager m_input;
	private PositionWrapper m_position;
	private RotationWrapper m_rotation;

	private long m_spawnInterval;
	private double m_velocity;

	public GunLogic(Entity spawn, InputManager input, long spawnInterval, double velocity) {
		super(new Aspect(TypeManager.getType(PositionWrapper.class), TypeManager.getType(RotationWrapper.class)));
		m_spawningEntity = spawn;
		m_input = input;
		m_spawnInterval = spawnInterval;
		m_velocity = velocity;
	}

	public GunLogic(Entity parent, Entity spawn, InputManager input, long spawnInterval, double velocity) {
		super(parent,
				new Aspect(TypeManager.getType(PositionWrapper.class), TypeManager.getType(RotationWrapper.class)));
		m_spawningEntity = spawn;
		m_input = input;
		m_spawnInterval = spawnInterval;
		m_velocity = velocity;
	}

	// ticks since last spawn
	private long ticksElapsed = 0;

	@Override
	public ArrayList<Entity> spawn(int ticks) {
		ticksElapsed += ticks;
		if (ticksElapsed > m_spawnInterval && m_input.getValue("RightMouse") == 1.0f) {
			ArrayList<Entity> spawn = new ArrayList<Entity>();
			spawn.add(makeBullet(-10));
			spawn.add(makeBullet(0));
			spawn.add(makeBullet(10));
			ticksElapsed = 0;
			return spawn;
		}
		return new ArrayList<Entity>();
	}

	public Entity makeBullet(double offset) {
		Entity spawn = m_spawningEntity.copy();
		PhysicsData physics = (PhysicsData) spawn.getComponent(TypeManager.getType(PhysicsData.class));
		physics.setPosition(m_position.getPosition().add(
				Vector.dyn4jNormalVector(m_rotation.getRotation()).scalarMultiply(100)));
		physics.setRotation(m_rotation.getRotation());
		physics.setVelocity(Vector.dyn4jNormalVector(m_rotation.getRotation() + offset).scalarMultiply(m_velocity));
		physics.translateCenter(Vector.dyn4jNormalVector(m_rotation.getRotation() + offset).scalarMultiply(50));

		physics.setMass(1000);
		return spawn;
	}

	@Override
	public void loadDependencies() {
		m_position = (PositionWrapper) this.loadDependency(TypeManager.getType(PositionWrapper.class));
		m_rotation = (RotationWrapper) this.loadDependency(TypeManager.getType(RotationWrapper.class));
	}

	@Override
	public Component copy() {
		return new GunLogic(m_spawningEntity, m_input, m_spawnInterval, m_velocity);
	}
}
