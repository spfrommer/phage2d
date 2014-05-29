package engine.core.implementation.behavior.base;


/**
 * An executable tree of Nodes.
 */
public class LogicTree {
	/**
	 * The root node of the tree.
	 */
	private Node m_root;

	public LogicTree() {

	}

	/**
	 * Updates the root Node by the given number of ticks.
	 * 
	 * @param ticks
	 */
	public void update(int ticks) {
		m_root.update(ticks);
	}

	public void setRootNode(Node root) {
		m_root = root;
	}

	public Node getRootNode() {
		return m_root;
	}
}
