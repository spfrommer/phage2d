package engine.core.implementation.behavior.logic;

import engine.core.framework.component.LogicComponent;
import engine.core.implementation.behavior.base.Node;
import engine.core.implementation.behavior.base.NodeContext;

/**
 * An executable tree of Nodes.
 */
public class TreeLogic extends LogicComponent {
	/**
	 * The root node of the tree.
	 */
	private Node m_root;
	/**
	 * The NodeContext of tree.
	 */
	private NodeContext m_context;

	public TreeLogic() {
		super();
	}

	/**
	 * Updates the root Node by the given number of ticks.
	 * 
	 * @param ticks
	 */
	public void update(int ticks) {
		if (m_context == null) {
			m_context = new NodeContext(this.getEntity().getContext());
			m_root.setContext(m_context);
		}
		m_root.update(ticks);
	}

	public void setRoot(Node root) {
		m_root = root;
	}

	public Node getRoot() {
		return m_root;
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
