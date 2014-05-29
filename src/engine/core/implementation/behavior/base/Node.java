package engine.core.implementation.behavior.base;

import engine.core.framework.Entity;

/**
 * A node in a tree - can be either a composite node or a leaf node.
 */
public interface Node {
	/**
	 * Loads all required components from the Entity.
	 * 
	 * @param entity
	 * @return whether all required Components could be loaded from the Entity
	 */
	public boolean load(Entity entity);

	/**
	 * Updates the Node - a composite node will simply call this method on its children.
	 * 
	 * @param ticks
	 * @return
	 */
	public ExecutionState update(int ticks);
}
