package engine.core.implementation.behavior.base.composite;

import java.util.ArrayList;
import java.util.List;

import engine.core.implementation.behavior.base.Node;

/**
 * A Node that contains multiple other Nodes.
 */
public abstract class Composite implements Node {
	private List<Node> m_children;
	{
		m_children = new ArrayList<Node>();
	}

	public void add(Node node) {
		m_children.add(node);
	}

	public void remove(Node node) {
		m_children.remove(node);
	}
}
