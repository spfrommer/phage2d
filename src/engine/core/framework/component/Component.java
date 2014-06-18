package engine.core.framework.component;

import engine.core.framework.Entity;

/**
 * A Component that can be added to an entity
 */
public abstract class Component {
	/**
	 * The Entity that this Component belongs to
	 */
	private Entity m_parent;

	Component() {
	}

	/**
	 * Sets the Entity this Component belongs to
	 * 
	 * @param parent
	 */
	public void setEntity(Entity parent) {
		m_parent = parent;
	}

	/**
	 * Returns the Entity that contains this Component
	 * 
	 * @return
	 */
	protected Entity getEntity() {
		return m_parent;
	}

	/**
	 * Returns a deep clone of the Component
	 * 
	 * @return
	 */
	public abstract Component copy();
}
