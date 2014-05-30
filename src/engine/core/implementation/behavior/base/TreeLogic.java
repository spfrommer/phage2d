package engine.core.implementation.behavior.base;

import engine.core.framework.Entity;
import engine.core.framework.component.LogicComponent;

/**
 * An executable tree of Nodes.
 */
public class TreeLogic extends LogicComponent {
	/**
	 * The root node of the tree.
	 */
	private Node m_root;

	public TreeLogic() {

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

	public void load(Entity entity) {
		m_root.load(entity);
	}

	@Override
	public TreeLogic copy() {
		TreeLogic newTree = new TreeLogic();
		newTree.setRootNode(m_root.copy());
		return newTree;
	}

	@Override
	public void loadDependencies() {
		m_root.load(this.getEntity());
	}
}
