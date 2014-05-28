package engine.core.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemAspectManager {
	private EntitySystem m_system;
	private Map<Aspect, List<Entity>> m_aspectLists;
	private List<AspectSystemListener> m_listeners;

	{
		m_aspectLists = new HashMap<Aspect, List<Entity>>();
		m_listeners = new ArrayList<AspectSystemListener>();
	}

	SystemAspectManager(EntitySystem system) {
		m_system = system;
		system.addListener(new SystemListener() {
			@Override
			public void entityAdded(Entity entity) {
				for (Aspect aspect : m_aspectLists.keySet()) {
					if (entity.getAspect().encapsulates(aspect))
						m_aspectLists.get(aspect).add(entity);
				}

				for (AspectSystemListener listener : m_listeners)
					listener.entityAdded(entity);
			}

			@Override
			public void entityRemoved(Entity entity) {
				for (Aspect aspect : m_aspectLists.keySet())
					m_aspectLists.get(aspect).remove(entity);

				for (AspectSystemListener listener : m_listeners)
					listener.entityRemoved(entity);
			}
		});
	}

	/**
	 * Adds a List for a certain Aspect - if the List is already created, the method will return
	 * 
	 * @param aspect
	 */
	public void loadAspect(Aspect aspect) {
		if (m_aspectLists.containsKey(aspect))
			return;

		if (!m_aspectLists.containsKey(aspect)) {
			List<Entity> entities = m_system.bulkReadAspect(aspect);
			m_aspectLists.put(aspect, entities);
		}
	}

	/**
	 * Adds a listener for a certain Aspect
	 * 
	 * @param listener
	 */
	public void addAspectListener(AspectSystemListener listener) {
		m_listeners.add(listener);
	}

	/**
	 * Gets all the Entities listed as encapsulating an Aspect
	 * 
	 * @param aspect
	 * @return
	 */
	public List<Entity> getEntities(Aspect aspect) {
		return m_aspectLists.get(aspect);
	}
}
