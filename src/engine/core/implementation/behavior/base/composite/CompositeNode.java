package engine.core.implementation.behavior.base.composite;

import java.util.ArrayList;
import java.util.List;

import engine.core.implementation.behavior.base.Node;
import engine.core.implementation.behavior.base.NodeContext;

/**
 * A Node that contains multiple other Nodes.
 */
public abstract class CompositeNode extends Node {
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

	public void addChild(Node node) {
		m_children.add(node);
		if (this.getContext() != null)
			node.setContext(this.getContext());
	}

	public void removeChild(Node node) {
		m_children.remove(node);
		node.setContext(null);
	}

	protected List<Node> getChildren() {
		return m_children;
	}

	@Override
	public void setContext(NodeContext context) {
		super.setContext(context);
		for (Node child : m_children)
			child.setContext(context);
	}
}
