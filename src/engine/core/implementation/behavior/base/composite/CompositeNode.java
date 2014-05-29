package engine.core.implementation.behavior.base.composite;

import java.util.ArrayList;
import java.util.List;

import engine.core.implementation.behavior.base.Node;

/**
 * A Node that contains multiple other Nodes.
 */
public abstract class CompositeNode implements Node {
	/**
	 * The children of this Node.
	 */
	private List<Node> m_children;
	/**
	 * Stores the running Node, if there is one.
	 */
	private Node m_runningNode;

	{
		m_children = new ArrayList<Node>();
	}

	protected void setRunningNode(Node node) {
		m_runningNode = node;
	}

	protected Node getRunningNode() {
		return m_runningNode;
	}

	public void add(Node node) {
		m_children.add(node);
	}

	public void remove(Node node) {
		m_children.remove(node);
	}

	protected List<Node> getChildren() {
		return m_children;
	}
}
