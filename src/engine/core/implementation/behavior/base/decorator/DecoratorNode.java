package engine.core.implementation.behavior.base.decorator;

import engine.core.framework.Entity;
import engine.core.implementation.behavior.base.Node;

/**
 * A Node with one child and affects the running state of that child - puts on a max run time, etc.
 */
public abstract class DecoratorNode implements Node {
	private Node m_child;

	public DecoratorNode() {

	}

	public DecoratorNode(Node child) {
		m_child = child;
	}

	public void setChild(Node child) {
		m_child = child;
	}

	public Node getChild() {
		return m_child;
	}

	@Override
	public boolean load(Entity entity) {
		return m_child.load(entity);
	}
}
