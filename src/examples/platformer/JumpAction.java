package examples.platformer;

import utils.physics.Vector;
import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;
import engine.core.implementation.behavior.base.leaf.action.ActionLeaf;
import engine.core.implementation.physics.data.PhysicsData;

/**
 * Will make the ball jump - return SUCCESS if the ball completed the jump and RUNNING if it is jumping.
 */
public class JumpAction extends ActionLeaf {
	private PhysicsData m_physics;
	// Our velocity one tick ago
	private int m_atRestTicks;

	public JumpAction() {
		super();
	}

	@Override
	public ExecutionState update(int ticks) {
		if (Math.abs(m_physics.getVelocity().getY()) < 50) {
			m_atRestTicks += ticks;
			if (m_atRestTicks >= 30) {
				m_atRestTicks = 0;
				m_physics.applyForce(new Vector(0, 100000000));
				return ExecutionState.SUCCESS;
			}
		}

		return ExecutionState.RUNNING;
	}

	@Override
	public boolean load(Entity entity) {
		m_physics = (PhysicsData) entity.getComponent(TypeManager.getType(PhysicsData.class));
		return true;
	}

	@Override
	public Node copy() {
		return new JumpAction();
	}
}
