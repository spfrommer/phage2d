package engine.core.framework;

/**
 * Listens to Entity added and removed calls from the EntitySystem.
 */
interface SystemListener {
	public void entityAdded(Entity entity);

	public void entityRemoved(Entity entity);
}
