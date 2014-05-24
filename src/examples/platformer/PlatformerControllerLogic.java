package examples.platformer;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.control.logic.ControllerLogic;
import engine.core.implementation.physics.data.PhysicsData;
import engine.inputs.InputManager;

/**
 * A controller for a bouncing ball.
 */
public class PlatformerControllerLogic extends ControllerLogic {
	private PhysicsData m_physics;
	private InputManager m_inputManager;

	public PlatformerControllerLogic(InputManager manager) {
		super(new Aspect(TypeManager.getType(PhysicsData.class)));
		m_inputManager = manager;
	}

	public PlatformerControllerLogic(Entity parent, InputManager manager) {
		super(parent, new Aspect(TypeManager.getType(PhysicsData.class)));
		m_inputManager = manager;
	}

	@Override
	public void update(int ticks) {
		float jump = m_inputManager.getValue("Jump");

		float left = m_inputManager.getValue("Left");
		float right = m_inputManager.getValue("Right");

		float strafe = left - right;

		if (Math.abs(m_physics.getVelocity().getY()) < 0.1)
			m_physics.applyForce(new Vector(0, jump * 100000000));

		m_physics.applyTorque(strafe * 20000000);
	}

	@Override
	public void loadDependencies() {
		m_physics = (PhysicsData) this.loadDependency(TypeManager.getType(PhysicsData.class));
	}

	@Override
	public Component copy() {
		return new PlatformerControllerLogic(m_inputManager);
	}
}
