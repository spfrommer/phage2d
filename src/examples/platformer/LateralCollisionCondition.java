package examples.platformer;

import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;
import engine.core.implementation.behavior.base.leaf.ConditionLeaf;
import engine.core.implementation.physics.data.PhysicsData;

/**
 * Returns SUCCESS if lateral collision detected and FAILURE if not.
 */
public class LateralCollisionCondition extends ConditionLeaf {
	private PhysicsData m_physics;
	private double m_lastXVelocity;

	@Override
	public boolean load(Entity entity) {
		try {
			m_physics = (PhysicsData) entity.getComponent(TypeManager.typeOf(PhysicsData.class));
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Node copy() {
		return new LateralCollisionCondition();
	}

	@Override
	public ExecutionState update(int ticks) {
		double newXVelocity = m_physics.getVelocity().getX();
		if (sign(m_lastXVelocity) != 0 && sign(newXVelocity) != 0 && sign(m_lastXVelocity) != sign(newXVelocity)) {
			return ExecutionState.SUCCESS;
		}
		m_lastXVelocity = newXVelocity;
		return ExecutionState.FAILURE;
	}

	private static int sign(double f) {
		if (f != f)
			throw new IllegalArgumentException("NaN");
		if (f == 0)
			return 0;
		f *= Double.POSITIVE_INFINITY;
		if (f == Double.POSITIVE_INFINITY)
			return 1;
		if (f == Double.NEGATIVE_INFINITY)
			return -1;

		throw new IllegalArgumentException("Unfathomed double");
	}
}
