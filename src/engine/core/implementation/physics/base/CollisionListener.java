package engine.core.implementation.physics.base;

import engine.core.framework.Entity;

/**
 * An interface for ListenerCollisionHandlerLogic.
 */
public interface CollisionListener {
	public void collided(Entity thisEntity, Entity otherEntity);
}
