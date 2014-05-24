package engine.core.framework;

import java.util.List;

/**
 * An AspectActivity iterates over all the elements in an EntitySystem that encapsulate a given Aspect.
 * 
 */
public abstract class AspectActivity {
	/**
	 * The EntitySystem this is acting on.
	 */
	private EntitySystem m_system;
	/**
	 * The Aspect that defines which Entities to act on.
	 */
	private Aspect m_aspect;

	/**
	 * Makes a new AspectActivity that reads all existing Entities and listens for new ones.
	 * 
	 * @param system
	 * @param aspect
	 */
	public AspectActivity(EntitySystem system, Aspect aspect) {
		m_system = system;
		m_aspect = aspect;

		m_system.getAspectManager().loadAspect(m_aspect);

		// Make sure we catch new Entities that match our Aspect so we can act on them.
		m_system.getAspectManager().addAspectListener(new AspectSystemListener(aspect) {
			@Override
			public void entityAddedTriggered(Entity entity) {
				AspectActivity.this.entityAdded(entity);
			}

			@Override
			public void entityRemovedTriggered(Entity entity) {
				AspectActivity.this.entityRemoved(entity);
			}
		});
	}

	/**
	 * Calls entityAdded() on all Entities in the EntitySystem that have the correct Aspect. Useful if there are already
	 * Entities in the EntitySystem before this Activity was added.
	 */
	public void loadEntities() {
		for (Entity entity : m_system.getAspectManager().getEntities(m_aspect))
			this.entityAdded(entity);
	}

	/**
	 * Gets all the Entities that encapsulate this Activity's Aspect.
	 * 
	 * @return
	 */
	protected List<Entity> getEntities() {
		return m_system.getAspectManager().getEntities(m_aspect);
	}

	/**
	 * Get the EntitySystem this is acting on.
	 * 
	 * @return
	 */
	protected EntitySystem getSystem() {
		return m_system;
	}

	/**
	 * Called when an Entity is added to the EntitySystem that encapsulates this Aspect.
	 * 
	 * @param entity
	 */
	public abstract void entityAdded(Entity entity);

	/**
	 * Called when an Entity is removed with this Aspect.
	 * 
	 * @param entity
	 */
	public abstract void entityRemoved(Entity entity);
}
