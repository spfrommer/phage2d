package engine.core.implementation.behavior.base.composite;

import java.util.ArrayList;
import java.util.List;

import test.tree.FailureNode;
import test.tree.RunningNode;
import test.tree.SuccessNode;
import engine.core.framework.Entity;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;

/**
 * A ProbabilitySelectorComposite will visit one of its children semi-randomly based on probabilities. If a selected
 * node fails, another node is selected. To do this, a number between zero and the sum of the probabilities is
 * generated, then the section of the probability array it lands on will determine the selected node. An array with all
 * equal probabilities would make a truly random selection. The number of elements in the probability array should equal
 * the number of child nodes. See update method for more details on the selection algorithm.
 * 
 * The ProbabilitySelectorComposite is guaranteed to only call update on ONE of its children nodes for every update call
 * on it.
 */
public class ProbabilitySelectorComposite extends CompositeNode {
	private double[] m_probabilities;

	// Which nodes failed when we tried to update them.
	private List<Integer> m_triedNodes;

	{
		m_triedNodes = new ArrayList<Integer>();
	}

	/**
	 * Constructs a new ProbabilitySelectorComposite with the probability array initialized to {1, 1, 1, 1, 1}
	 */
	public ProbabilitySelectorComposite() {
		m_probabilities = new double[] { 1, 1, 1, 1, 1 };
	}

	/**
	 * Constructs a new ProbabilitySelectorComposite with an array of probabilities.
	 * 
	 * @param probabilities
	 */
	public ProbabilitySelectorComposite(double[] probabilities) {
		m_probabilities = probabilities;
	}

	/**
	 * Sets the probabilities of this Composite.
	 * 
	 * @param probabilities
	 */
	public void setProbabilities(double[] probabilities) {
		m_probabilities = probabilities;
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
		Node selected = this.getRunningNode();
		if (selected == null) {
			// pick new Node
			int selection = randomSelection();
			if (selection == -1)
				return ExecutionState.FAILURE;
			selected = this.getChildren().get(selection);
		}

		ExecutionState update = updateNode(selected, ticks);
		if (update == ExecutionState.FAILURE) {
			m_triedNodes.add(this.getChildren().indexOf(selected));

			return ExecutionState.RUNNING;
		} else {
			m_triedNodes.clear();
		}

		return update;
	}

	private ExecutionState updateNode(Node node, int ticks) {
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
			this.setRunningNode(null);
			return ExecutionState.SUCCESS;
		}
		default:
			break;
		}
		System.err.println("ProbabilitySelector should not reach here!");
		return null;
	}

	private int randomSelection() {
		double sum = 0;
		for (int i = 0; i < m_probabilities.length; i++) {
			if (!m_triedNodes.contains(i))
				sum += m_probabilities[i];
		}

		// random number between zero and the sum of the probabilities
		double random = Math.random() * sum;
		double runningSum = 0;
		for (int i = 0; i < m_probabilities.length; i++) {
			if (!m_triedNodes.contains(i)) {
				runningSum += m_probabilities[i];
				if (runningSum >= random) {
					return i;
				}
			}
		}
		System.err.println("Could not find random selection!");
		return -1;
	}

	public static void main(String[] args) {
		ProbabilitySelectorComposite selector = new ProbabilitySelectorComposite(new double[] { 0.33, 0.33, 0.00001 });

		selector.add(new FailureNode());
		selector.add(new RunningNode());
		selector.add(new SuccessNode());
		for (int i = 0; i < 10; i++) {
			System.out.println(selector.update(1));
		}
	}
}
