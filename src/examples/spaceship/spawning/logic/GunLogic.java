package examples.spaceship.spawning.logic;

import java.util.ArrayList;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.wrappers.TransformWrapper;
import engine.inputs.InputManager;

/**
 * Spawns a bullet entity at a certain position and rotation.
 * 
 * @eng.dependencies TransformWrapper
 */
public class GunLogic extends SpawningLogic {
	private Entity m_spawningEntity;
	private InputManager m_input;
	private TransformWrapper m_transform;

	private long m_spawnInterval;
	private double m_velocity;

	// ticks since last spawn
	private long ticksElapsed = 0;

	public GunLogic(Entity spawn, InputManager input, long spawnInterval, double velocity) {
		super(new Aspect(TypeManager.getType(TransformWrapper.class)));
		m_spawningEntity = spawn;
		m_input = input;
		m_spawnInterval = spawnInterval;
		m_velocity = velocity;
	}

	public GunLogic(Entity parent, Entity spawn, InputManager input, long spawnInterval, double velocity) {
		super(parent, new Aspect(TypeManager.getType(TransformWrapper.class)));
		m_spawningEntity = spawn;
		m_input = input;
		m_spawnInterval = spawnInterval;
		m_velocity = velocity;
	}

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

	/**
	 * Makes a bullet with a certain degree offset from the desired direction.
	 * 
	 * @param offset
	 * @return
	 */
	public Entity makeBullet(double offset) {
		Entity spawn = m_spawningEntity.copy();

		PhysicsData physics = (PhysicsData) spawn.getComponent(TypeManager.getType(PhysicsData.class));
		physics.setPosition(m_transform.getPosition().add(
				Vector.dyn4jNormalVector(m_transform.getRotation()).scalarMultiply(100)));
		physics.setRotation(m_transform.getRotation());
		physics.setVelocity(Vector.dyn4jNormalVector(m_transform.getRotation() + offset).scalarMultiply(m_velocity));
		physics.translateCenter(Vector.dyn4jNormalVector(m_transform.getRotation() + offset).scalarMultiply(50));

		physics.setMass(1000);
		return spawn;
	}

	@Override
	public void loadDependencies() {
		m_transform = (TransformWrapper) this.loadDependency(TypeManager.getType(TransformWrapper.class));
	}

	@Override
	public Component copy() {
		return new GunLogic(m_spawningEntity, m_input, m_spawnInterval, m_velocity);
	}
}
