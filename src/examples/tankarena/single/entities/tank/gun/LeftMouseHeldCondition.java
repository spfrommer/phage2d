package examples.tankarena.single.entities.tank.gun;

import engine.core.framework.Entity;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;
import engine.core.implementation.behavior.base.leaf.ConditionLeaf;
import engine.inputs.InputManager;

/**
 * Checks if the left mouse button is held down.
 */
public class LeftMouseHeldCondition extends ConditionLeaf {
	private InputManager m_input;

	public LeftMouseHeldCondition(InputManager input) {
		m_input = input;
	}

	@Override
	public boolean load(Entity entity) {
		return true;
	}

	@Override
	public Node copy() {
		return new LeftMouseHeldCondition(m_input);
	}

	@Override
	public ExecutionState update(int ticks) {
		float leftMouse = m_input.getValue("LeftMouse");
		if (leftMouse != 0f)
			return ExecutionState.SUCCESS;

		return ExecutionState.FAILURE;
	}
}
