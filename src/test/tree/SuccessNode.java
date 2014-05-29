package test.tree;

import engine.core.framework.Entity;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.leaf.LeafNode;

public class SuccessNode extends LeafNode {
	@Override
	public void load(Entity entity) {

	}

	@Override
	public ExecutionState update(int ticks) {
		return ExecutionState.SUCCESS;
	}
}
