package engine.core.framework;

import java.util.ArrayList;
import java.util.List;

public class EntitySystem {
	private List<Entity> m_entities;
	private List<SystemListener> m_listeners;
	private SystemAspectManager m_manager;

	private List<Entity> m_addQueue;
	private List<Entity> m_removeQueue;

	{
		m_entities = new ArrayList<Entity>();
		m_listeners = new ArrayList<SystemListener>();
		m_manager = new SystemAspectManager(this);
		m_addQueue = new ArrayList<Entity>();
		m_removeQueue = new ArrayList<Entity>();
	}

	/**
	 * Constructs an empty EntitySystem.
	 */
	public EntitySystem() {

	}

	void addListener(SystemListener listener) {
		m_listeners.add(listener);
	}

	void removeListener(SystemListener listener) {
		m_listeners.remove(listener);
	}

	/**
	 * Proccesses the add and remove queues and notifies listeners
	 */
	public void update() {
		synchronized (m_addQueue) {
			for (Entity entity : m_addQueue) {
				m_entities.add(entity);

				for (SystemListener esl : m_listeners)
					esl.entityAdded(entity);
			}
			m_addQueue.clear();
		}
		synchronized (m_removeQueue) {
			// System.out.println(m_entities.size());
			for (Entity entity : m_removeQueue) {
				m_entities.remove(entity);

				for (SystemListener esl : m_listeners)
					esl.entityRemoved(entity);
			}
			// System.out.println(m_entities.size());
			m_removeQueue.clear();
		}
	}

	/**
	 * Adds an Entity to the add queue
	 * 
	 * @param entity
	 */
	public void addEntity(Entity entity) {
		synchronized (m_addQueue) {
			m_addQueue.add(entity);
		}
	}

	/**
	 * Queues a removal of the Entity
	 * 
	 * @param entity
	 */
	public void removeEntity(Entity entity) {
		// Thread.dumpStack();
		synchronized (m_removeQueue) {
			m_removeQueue.add(entity);
		}
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
	 * Removes all Entities from the system.
	 */
	public void removeAllEntities() {
		int size = m_entities.size();
		for (int i = 0; i < size; i++)
			removeEntity(m_entities.get(0));
	}

	/**
	 * Gets all the Entities in the system.
	 * 
	 * @return
	 */
	public List<Entity> getAllEntities() {
		return m_entities;
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