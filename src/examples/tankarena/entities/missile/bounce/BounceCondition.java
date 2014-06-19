package examples.tankarena.entities.missile.bounce;

import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;
import engine.core.implementation.behavior.base.leaf.ConditionLeaf;

public class BounceCondition extends ConditionLeaf {
	private BounceData m_bounce;

	public BounceCondition() {
		super();
	}

	@Override
	public boolean load(Entity entity) {
		m_bounce = (BounceData) entity.getComponent(TypeManager.getType(BounceData.class));
		return true;
	}

	@Override
	public Node copy() {
		return new BounceCondition();
	}

	@Override
	public ExecutionState update(int ticks) {
		if (m_bounce.bounceCount >= m_bounce.maxBounces) {
			return ExecutionState.SUCCESS;
		} else {
			return ExecutionState.FAILURE;
		}
	}
}
