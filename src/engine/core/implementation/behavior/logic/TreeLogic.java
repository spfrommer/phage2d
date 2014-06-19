package engine.core.implementation.behavior.logic;

import engine.core.framework.Entity;
import engine.core.framework.component.LogicComponent;
import engine.core.implementation.behavior.base.Node;

/**
 * An executable tree of Nodes.
 */
public class TreeLogic extends LogicComponent {
	/**
	 * The root node of the tree.
	 */
	private Node m_root;

	public TreeLogic() {
		super();
	}

	/**
	 * Updates the root Node by the given number of ticks.
	 * 
	 * @param ticks
	 */
	public void update(int ticks) {
		m_root.update(ticks);
	}

	public void setRoot(Node root) {
		m_root = root;
	}

	public Node getRoot() {
		return m_root;
	}

	public void load(Entity entity) {
		m_root.load(entity);
	}

	@Override
	public TreeLogic copy() {
		TreeLogic newTree = new TreeLogic();
		newTree.setRoot(m_root.copy());
		return newTree;
	}

	@Override
	public void loadDependencies() {
		m_root.load(this.getEntity());
	}
}
