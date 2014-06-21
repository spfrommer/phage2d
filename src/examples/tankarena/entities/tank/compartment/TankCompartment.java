package examples.tankarena.entities.tank.compartment;

import org.dyn4j.geometry.Rectangle;

import utils.physics.JointType;
import utils.physics.Vector;
import engine.core.factory.ComponentFactory;
import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.activities.PhysicsActivity;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.logic.filter.NoneCollisionFilterLogic;
import examples.tankarena.entities.tank.Tank;
import examples.tankarena.entities.tank.TankComponent;
import examples.tankarena.entities.tank.body.TankBody;

/**
 * An invisible compartment for carrying Entities.
 */
public class TankCompartment extends Entity implements TankComponent {
	private Tank m_tank;
	private PhysicsActivity m_physics;

	public TankCompartment(Vector position, TankBody body, PhysicsActivity physicsActivity) {
		PhysicsData tankPhysics = (PhysicsData) body.getComponent(TypeManager.getType(PhysicsData.class));

		PhysicsData physics = ComponentFactory.addPhysicsData(this, tankPhysics.getPosition().add(position), 0,
				new Rectangle(0, 0));

		this.addComponent(new NoneCollisionFilterLogic());
		ComponentFactory.addNameData(this, "tankcompartment");

		m_physics.addJoint(PhysicsData.JointFactory.createJoint(tankPhysics, physics,
				tankPhysics.getPosition().add(position), JointType.WELD));
	}

	/**
	 * Sets the Tank this component belongs to.
	 * 
	 * @param tank
	 */
	@Override
	public void setTank(Tank tank) {
		m_tank = tank;
	}

	/**
	 * Gets the Tank this component belongs to.
	 * 
	 * @return
	 */
	@Override
	public Tank getTank() {
		return m_tank;
	}
}
