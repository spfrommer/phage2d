package examples.tankarena.entities.tank;

import utils.physics.Vector;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsData;
import examples.tankarena.entities.tank.body.TankBody;
import examples.tankarena.entities.tank.gun.TankGun;
import examples.tankarena.entities.tank.tread.TankTread;

public class Tank {
	private TankBody m_body;
	private TankTread m_leftTread;
	private TankTread m_rightTread;
	private TankGun m_gun;

	private double m_health;

	private EntitySystem m_system;
	private Vector m_spawnPosition;

	{
		m_health = 100;
		m_spawnPosition = new Vector(0, 0);
	}

	public Tank(EntitySystem system, TankBody body, TankTread leftTread, TankTread rightTread, TankGun gun) {
		m_body = body;
		m_leftTread = leftTread;
		m_rightTread = rightTread;
		m_gun = gun;

		m_body.setTank(this);
		m_leftTread.setTank(this);
		m_rightTread.setTank(this);
		m_gun.setTank(this);

		m_system = system;
	}

	public Tank(EntitySystem system, TankBody.BodyBuilder bodyBuilder, TankTread.TreadBuilder leftTreadBuilder,
			TankTread.TreadBuilder rightTreadBuilder, TankGun.GunBuilder gunBuilder) {
		m_body = bodyBuilder.build();
		m_leftTread = leftTreadBuilder.build();
		m_rightTread = rightTreadBuilder.build();
		m_gun = gunBuilder.build();

		m_body.setTank(this);
		m_leftTread.setTank(this);
		m_rightTread.setTank(this);
		m_gun.setTank(this);

		m_system = system;
	}

	/**
	 * Sets the position that the Tank should spawn at when it dies.
	 * 
	 * @param position
	 */
	public void setSpawnPosition(Vector position) {
		m_spawnPosition = position;
	}

	/**
	 * Damage the tank - called by ComponentDamageHandlerLogic
	 * 
	 * @param damage
	 */
	public void damage(double damage) {
		m_health -= damage;
		if (m_health <= 0) {
			PhysicsData bodyPhysics = (PhysicsData) m_body.getComponent(TypeManager.typeOf(PhysicsData.class));
			bodyPhysics.setPosition(m_spawnPosition.clone());

			PhysicsData leftTreadPhysics = (PhysicsData) m_leftTread.getComponent(TypeManager
					.typeOf(PhysicsData.class));
			leftTreadPhysics.setPosition(m_spawnPosition.clone());

			PhysicsData rightTreadPhysics = (PhysicsData) m_rightTread.getComponent(TypeManager
					.typeOf(PhysicsData.class));
			rightTreadPhysics.setPosition(m_spawnPosition.clone());

			PhysicsData gunPhysics = (PhysicsData) m_gun.getComponent(TypeManager.typeOf(PhysicsData.class));
			gunPhysics.setPosition(m_spawnPosition.clone());

			m_health = 100;
		}
	}
}
