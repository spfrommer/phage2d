package engine.core.implementation.behavior.base.composite;

import engine.core.framework.Entity;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;

/**
 * Updates all its child nodes simultaneously. If all update successfully, SUCCESS is returned. If at least one fails,
 * FAILURE is returned. If at least one returns running, and none fail, RUNNING is returned;
 */
public class ParallelComposite extends CompositeNode {
	public ParallelComposite() {

	}

	@Override
	public boolean load(Entity entity) {
		for (Node n : this.getChildren()) {
			if (n.load(entity) == false)
				return false;
		}
		return true;
	}

	@Override
	public Node copy() {
		ParallelComposite parallel = new ParallelComposite();
		for (Node n : this.getChildren())
			parallel.addChild(n.copy());
		return parallel;
	}

	@Override
	public ExecutionState update(int ticks) {
		ExecutionState returnState = ExecutionState.SUCCESS;
		for (Node n : this.getChildren()) {
			ExecutionState state = n.update(ticks);
			if (state == ExecutionState.FAILURE)
				returnState = ExecutionState.FAILURE;
			if (state == ExecutionState.RUNNING && returnState == ExecutionState.SUCCESS)
				returnState = ExecutionState.RUNNING;
		}
		return returnState;
	}
}
