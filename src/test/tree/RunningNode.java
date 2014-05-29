package test.tree;

import engine.core.framework.Entity;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.leaf.LeafNode;

public class RunningNode extends LeafNode {
	@Override
	public boolean load(Entity entity) {
		return true;
	}

	@Override
	public ExecutionState update(int ticks) {
		return ExecutionState.RUNNING;
	}
}
