package engine.core.framework;

import java.util.ArrayList;
import java.util.List;

import utils.collections.TwoWayBuffer;

/**
 * Holds a group of Entities.
 */
public class EntitySystem {
	private List<Entity> m_entities;
	private List<SystemListener> m_listeners;
	private SystemAspectManager m_manager;

	private TwoWayBuffer<Entity> m_buffer;

	private EntityContext m_context;

	{
		m_entities = new ArrayList<Entity>();
		m_listeners = new ArrayList<SystemListener>();
		m_manager = new SystemAspectManager(this);
		m_buffer = new TwoWayBuffer<Entity>();

		m_context = new EntityContext(this);
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
		m_buffer.lock();
		for (Entity entity : m_buffer.getAddBuffer()) {
			m_entities.add(entity);
			entity.setContext(m_context);

			for (SystemListener esl : m_listeners)
				esl.entityAdded(entity);
		}
		for (Entity entity : m_buffer.getRemoveBuffer()) {
			m_entities.remove(entity);
			entity.setContext(null);

			for (SystemListener esl : m_listeners)
				esl.entityRemoved(entity);
		}
		m_buffer.clear();
		m_buffer.unlock();
	}

	/**
	 * Adds an Entity to the add queue
	 * 
	 * @param entity
	 */
	public void addEntity(Entity entity) {
		m_buffer.bufferAdd(entity);
	}

	/**
	 * Queues a removal of the Entity
	 * 
	 * @param entity
	 */
	public void removeEntity(Entity entity) {
		m_buffer.bufferRemove(entity);
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