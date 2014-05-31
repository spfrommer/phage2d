package engine.core.implementation.behavior.base.leaf.action.executor;

import engine.core.implementation.behavior.base.ExecutionState;

public interface ActionExecutable {
	public ExecutionState update(int ticks);
}
