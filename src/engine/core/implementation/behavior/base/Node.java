package engine.core.implementation.behavior.base;

/**
 * A node in a tree - can be either a composite node or a leaf node.
 */
public interface Node {
	public ExecutionState update(int ticks);
}
