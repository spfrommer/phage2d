package engine.core.framework;

import java.util.ArrayList;
import java.util.List;

public class EntitySystem {
	private List<Entity> m_entities;
	private List<SystemListener> m_listeners;
	private SystemAspectManager m_manager;

	public EntitySystem() {
		m_entities = new ArrayList<Entity>();
		m_listeners = new ArrayList<SystemListener>();
		m_manager = new SystemAspectManager(this);
	}

	void addListener(SystemListener listener) {
		m_listeners.add(listener);
	}

	void removeListener(SystemListener listener) {
		m_listeners.remove(listener);
	}

	/**
	 * Adds an Entity to the EntitySystem and notifies SystemListeners
	 * 
	 * @param entity
	 */
	public void addEntity(Entity entity) {
		m_entities.add(entity);
		for (SystemListener esl : m_listeners)
			esl.entityAdded(entity);
	}

	/**
	 * Removes an Entity to the EntitySystem and notifies SystemListeners
	 * 
	 * @param entity
	 */
	public void removeEntity(Entity entity) {
		m_entities.remove(entity);

		for (SystemListener esl : m_listeners)
			esl.entityRemoved(entity);
	}

	/**
	 * Slow method that gathers all entities that have the given aspect
	 * 
	 * @param aspect
	 * @return
	 */
	List<Entity> bulkReadAspect(Aspect aspect) {
		ArrayList<Entity> entities = new ArrayList<Entity>();

		for (Entity entity : m_entities) {
			if (entity.getAspect().encapsulates(aspect)) {
				entities.add(entity);
			}
		}
		return entities;
	}

	/**
	 * Gets the SystemAspectManager of this EntitySystem
	 * 
	 * @return
	 */
	public SystemAspectManager getAspectManager() {
		return m_manager;
	}
}