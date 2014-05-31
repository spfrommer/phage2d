package examples.platformer;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.LogicComponent;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.leaf.action.executor.ActionExecutable;
import engine.core.implementation.physics.data.PhysicsData;
import engine.inputs.InputManager;

/**
 * A controller for a bouncing ball.
 */
public class ManualControllerLogic extends LogicComponent implements ActionExecutable {
	private PhysicsData m_physics;
	private InputManager m_inputManager;

	public ManualControllerLogic(InputManager manager) {
		super(new Aspect(TypeManager.getType(PhysicsData.class)));
		m_inputManager = manager;
	}

	public ManualControllerLogic(Entity parent, InputManager manager) {
		super(parent, new Aspect(TypeManager.getType(PhysicsData.class)));
		m_inputManager = manager;
	}

	@Override
	public ExecutionState update(int ticks) {
		float jump = m_inputManager.getValue("Jump");

		float left = m_inputManager.getValue("Left");
		float right = m_inputManager.getValue("Right");

		float strafe = left - right;

		if (Math.abs(m_physics.getVelocity().getY()) < 110)
			m_physics.applyForce(new Vector(0, jump * 100000000));

		m_physics.applyTorque(strafe * 20000000);
		return ExecutionState.RUNNING;
	}

	@Override
	public void loadDependencies() {
		m_physics = (PhysicsData) this.loadDependency(TypeManager.getType(PhysicsData.class));
	}

	@Override
	public Component copy() {
		return new ManualControllerLogic(m_inputManager);
	}
}
