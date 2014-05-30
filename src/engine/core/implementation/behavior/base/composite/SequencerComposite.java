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
	public Node copy() {
		SequencerComposite sequencer = new SequencerComposite();
		for (Node n : this.getChildren())
			sequencer.add(n);

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
			if (nodeIndex >= children.size())
				return ExecutionState.SUCCESS;
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

		sequencer.add(new SuccessNode());
		sequencer.add(new SuccessNode());
		sequencer.add(new RunningNode());
		sequencer.add(new FailureNode());
		for (int i = 0; i < 4; i++) {
			System.out.println(sequencer.update(1));
		}
	}
}
