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
	public Node copy() {
		SequencerComposite sequencer = new SequencerComposite();
		for (Node n : this.getChildren())
			sequencer.addChild(n);

		return sequencer;
	}

	@Override
	public ExecutionState update(int ticks) {
		List<Node> children = this.getChildren();
		Node running = this.getRunningNode();

		int nodeIndex = 0;
		if (running != null)
			nodeIndex = children.indexOf(running);

		Node node = children.get(nodeIndex);
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
			if (nodeIndex >= children.size() - 1) {
				this.setRunningNode(null);
				return ExecutionState.SUCCESS;
			}
			this.setRunningNode(children.get(nodeIndex + 1));
			return ExecutionState.RUNNING;
		}
		default:
			break;
		}
		System.err.println("SequencerComposite should not reach here!");
		return ExecutionState.SUCCESS;
	}

	public static void main(String[] args) {
		SequencerComposite sequencer = new SequencerComposite();

		sequencer.addChild(new SuccessNode());
		sequencer.addChild(new SuccessNode());
		sequencer.addChild(new RunningNode());
		sequencer.addChild(new FailureNode());
		for (int i = 0; i < 4; i++) {
			System.out.println(sequencer.update(1));
		}
	}
}
