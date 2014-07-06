package engine.core.implementation.behavior.base;

import engine.core.framework.EntityContext;

/**
 * The context of all Nodes in a tree.
 */
public class NodeContext {
	private EntityContext m_context;

	/**
	 * Constructs a NodeContext containing an EntityContext.
	 * 
	 * @param context
	 */
	public NodeContext(EntityContext context) {
		m_context = context;
	}

	/**
	 * Gets the EntityContext of the tree.
	 * 
	 * @return
	 */
	public EntityContext getEntityContext() {
		return m_context;
	}
}
