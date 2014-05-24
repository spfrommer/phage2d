package engine.core.framework;

/**
 * Listens to Entity added and removed calls from the EntitySystem, but only forwards the method call if the Entity's
 * Aspect encapsulates that of the filter.
 */
public abstract class AspectSystemListener implements SystemListener {
	// What aspect we have to filter for
	private Aspect m_aspectFilter;

	public AspectSystemListener(Aspect aspectFilter) {
		m_aspectFilter = aspectFilter;
	}

	/*
	 * Called if the aspect filter and filter type have matched
	 */
	public abstract void entityAddedTriggered(Entity entity);

	public abstract void entityRemovedTriggered(Entity entity);

	@Override
	public void entityAdded(Entity entity) {
		if (entity.getAspect().encapsulates(m_aspectFilter))
			entityAddedTriggered(entity);
	}

	@Override
	public void entityRemoved(Entity entity) {
		if (entity.getAspect().encapsulates(m_aspectFilter))
			entityRemovedTriggered(entity);
	}
}
