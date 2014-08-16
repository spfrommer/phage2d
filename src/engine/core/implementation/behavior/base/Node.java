package engine.core.implementation.behavior.base;

import engine.core.framework.Entity;

/**
 * A node in a tree - can be either a composite node or a leaf node.
 */
public abstract class Node {
	private NodeContext m_context;

	/**
	 * Sets the context of this Node.
	 * 
	 * @param context
	 */
	public void setContext(NodeContext context) {
		m_context = context;
	}

	/**
	 * Gets the context of this Node.
	 * 
	 * @return
	 */
	public NodeContext getContext() {
		return m_context;
	}

	/**
	 * Loads all required components from the Entity.
	 * 
	 * @param entity
	 * @return whether all required Components could be loaded from the Entity
	 */
	public abstract boolean load(Entity entity);

	/**
	 * Returns a copy of this Node.
	 * 
	 * @return
	 */
	public abstract Node copy();

	/**
	 * Updates the Node - a composite node will simply call this method on its children.
	 * 
	 * @param ticks
	 * @return
	 */
	public abstract ExecutionState update(int ticks);
}
