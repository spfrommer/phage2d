package engine.core.implementation.behavior.base.composite;

import test.tree.FailureNode;
import test.tree.RunningNode;
import test.tree.SuccessNode;
import engine.core.framework.Entity;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;

/**
 * Executes nodes in descending priority until one succeeds. The first integer in the priority index/array maps to the
 * index of the child with the highest priority, with the second integer mapping to the second highest priority, etc.
 */
public class PrioritySelectorComposite extends CompositeNode {
	private int[] m_priorityIndex;
	private int m_nextPick;

	{
		m_nextPick = 0;
	}

	/**
	 * Constructs a PrioritySelectorComposite with the priority index set to {1, 2, 3, 4, 5}.
	 */
	public PrioritySelectorComposite() {
		m_priorityIndex = new int[] { 1, 2, 3, 4, 5 };
	}

	/**
	 * Constructs a PrioritySelectorComposite with the given priority index.
	 * 
	 * @param priorityIndex
	 */
	public PrioritySelectorComposite(int[] priorityIndex) {
		m_priorityIndex = priorityIndex;
	}

	/**
	 * Sets the priorities of this Composite.
	 * 
	 * @param priorities
	 */
	public void setPriorities(int[] priorities) {
		m_priorityIndex = priorities;
	}

	@Override
	public void load(Entity entity) {

	}

	@Override
	public ExecutionState update(int ticks) {
		Node selected = this.getRunningNode();
		if (selected == null)
			selected = getNextPriority();

		ExecutionState state = selected.update(ticks);
		switch (state) {
		case FAILURE: {
			m_nextPick++;
			this.setRunningNode(null);
			return ExecutionState.RUNNING;
		}
		case RUNNING: {
			this.setRunningNode(selected);
			return ExecutionState.RUNNING;
		}
		case SUCCESS: {
			m_nextPick = 0;
			this.setRunningNode(null);
			return ExecutionState.SUCCESS;
		}
		default:
			break;
		}
		System.err.println("PrioritySelector should not reach here!");
		return null;
	}

	private Node getNextPriority() {
		return this.getChildren().get(m_priorityIndex[m_nextPick]);
	}

	public static void main(String[] args) {
		PrioritySelectorComposite priority = new PrioritySelectorComposite(new int[] { 2, 1, 0 });

		priority.add(new SuccessNode());
		priority.add(new RunningNode());
		priority.add(new FailureNode());

		for (int i = 0; i < 10; i++)
			System.out.println(priority.update(1));
	}
}
