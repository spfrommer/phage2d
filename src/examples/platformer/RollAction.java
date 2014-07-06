package examples.platformer;

import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;
import engine.core.implementation.behavior.base.leaf.action.ActionLeaf;
import engine.core.implementation.physics.data.PhysicsData;

/**
 * Applies a torque to an object - would best be used in conjunction with a TimeDecorator since it always returns
 * Running.
 */
public class RollAction extends ActionLeaf {
	private PhysicsData m_physics;
	private double m_torque;

	public RollAction(double torque) {
		super();
		m_torque = torque;
	}

	@Override
	public ExecutionState update(int ticks) {
		m_physics.applyTorque(m_torque);
		return ExecutionState.RUNNING;
	}

	@Override
	public boolean load(Entity entity) {
		m_physics = (PhysicsData) entity.getComponent(TypeManager.typeOf(PhysicsData.class));
		return true;
	}

	@Override
	public Node copy() {
		return new RollAction(m_torque);
	}
}
