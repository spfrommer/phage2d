package examples.tankarena.entities.missile.bounce;

import engine.core.framework.Entity;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;
import engine.core.implementation.behavior.base.leaf.ConditionLeaf;
import engine.core.implementation.extras.data.HealthData;

public class BounceCondition extends ConditionLeaf {
	private BounceData m_bounce;
	private ComponentType m_health;

	{
		m_health = TypeManager.getType(HealthData.class);
	}

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
		if (m_bounce.bouncedAgainst != null && m_bounce.bouncedAgainst.hasComponent(m_health)) {
			return ExecutionState.SUCCESS;
		}
		if (m_bounce.bounceCount >= m_bounce.maxBounces) {
			return ExecutionState.SUCCESS;
		}
		return ExecutionState.FAILURE;
	}
}
