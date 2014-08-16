package engine.core.framework;

/**
 * An immutable EntityContext which will be passed to an Entity from the EntitySystem. Contains the EntitySystem that
 * the Entity is a part of.
 */
public class EntityContext {
	private EntitySystem m_system;

	public EntityContext(EntitySystem system) {
		m_system = system;
	}

	public EntitySystem getSystem() {
		return m_system;
	}
}
