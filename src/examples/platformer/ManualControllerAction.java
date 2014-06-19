package examples.platformer;

import utils.physics.Vector;
import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;
import engine.core.implementation.behavior.base.leaf.action.ActionLeaf;
import engine.core.implementation.physics.data.PhysicsData;
import engine.inputs.InputManager;

/**
 * A controller for a bouncing ball.
 */
public class ManualControllerAction extends ActionLeaf {
	private PhysicsData m_physics;
	private InputManager m_inputManager;

	public ManualControllerAction(InputManager manager) {
		super();
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
	public boolean load(Entity entity) {
		m_physics = (PhysicsData) entity.getComponent(TypeManager.getType(PhysicsData.class));
		return true;
	}

	@Override
	public Node copy() {
		return new ManualControllerAction(m_inputManager);
	}
}
