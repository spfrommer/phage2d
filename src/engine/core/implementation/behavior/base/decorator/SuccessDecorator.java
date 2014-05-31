package engine.core.implementation.behavior.base.decorator;

import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;

/**
 * Forces a child to return SUCCESS.
 */
public class SuccessDecorator extends DecoratorNode {
	@Override
	public Node copy() {
		return new SuccessDecorator();
	}

	@Override
	public ExecutionState update(int ticks) {
		this.getChild().update(ticks);
		return ExecutionState.SUCCESS;
	}

}
