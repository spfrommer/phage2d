package examples.tankarena.entities.tank;

import examples.tankarena.entities.tank.body.TankBody;
import examples.tankarena.entities.tank.gun.TankGun;
import examples.tankarena.entities.tank.tread.TankTread;

public class Tank {
	private TankBody m_body;
	private TankTread m_leftTread;
	private TankTread m_rightTread;
	private TankGun m_gun;

	private double m_health = 100;

	public Tank(TankBody body, TankTread leftTread, TankTread rightTread, TankGun gun) {
		m_body = body;
		m_leftTread = leftTread;
		m_rightTread = rightTread;
		m_gun = gun;

		m_body.setTank(this);
		m_leftTread.setTank(this);
		m_rightTread.setTank(this);
		m_gun.setTank(this);
	}

	public Tank(TankBody.BodyBuilder bodyBuilder, TankTread.TreadBuilder leftTreadBuilder,
			TankTread.TreadBuilder rightTreadBuilder, TankGun.GunBuilder gunBuilder) {
		m_body = bodyBuilder.build();
		m_leftTread = leftTreadBuilder.build();
		m_rightTread = rightTreadBuilder.build();
		m_gun = gunBuilder.build();

		m_body.setTank(this);
		m_leftTread.setTank(this);
		m_rightTread.setTank(this);
		m_gun.setTank(this);
	}

	/**
	 * Damage the tank - called by ComponentDamageHandlerLogic
	 * 
	 * @param damage
	 */
	public void damage(double damage) {
		m_health -= damage;
		System.out.println(m_health);
	}
}
