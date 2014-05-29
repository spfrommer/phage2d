package engine.core.implementation.behavior.base.composite;

import java.util.List;

import test.tree.FailureNode;
import test.tree.RunningNode;
import test.tree.SuccessNode;
import engine.core.framework.Entity;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;

/**
 * A composite node that executes each of its children sequentially, returning FAILURE if any node returns FAILURE,
 * SUCCESS if all nodes succeed, and RUNNING if a Node returns RUNNING.
 * 
 * The SequencerComposite is guaranteed to only call update on ONE of its children nodes for every update call on it.
 */
public class SequencerComposite extends CompositeNode {
	public SequencerComposite() {

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
	public ExecutionState update(int ticks) {
		List<Node> children = this.getChildren();
		Node running = this.getRunningNode();

		int startNode = 0;
		if (running != null)
			startNode = children.indexOf(running);

		for (; startNode < children.size(); startNode++) {
			Node node = children.get(startNode);
			ExecutionState state = node.update(ticks);
			switch (state) {
			case FAILURE:
				this.setRunningNode(null);
				return ExecutionState.FAILURE;
			case RUNNING: {
				this.setRunningNode(node);
				return ExecutionState.RUNNING;
			}
			case SUCCESS: {
				this.setRunningNode(children.get(startNode + 1));
				return ExecutionState.RUNNING;
			}
			default:
				break;
			}
		}
		return ExecutionState.SUCCESS;
	}

	public static void main(String[] args) {
		SequencerComposite selector = new SequencerComposite();

		selector.add(new SuccessNode());
		selector.add(new SuccessNode());
		selector.add(new RunningNode());
		selector.add(new FailureNode());
		for (int i = 0; i < 4; i++) {
			System.out.println(selector.update(1));
		}
	}
}
