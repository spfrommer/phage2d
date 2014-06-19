package engine.core.implementation.physics.logic.handler;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.LogicComponent;

/**
 * Handles a collision between its Entity and another.
 */
public abstract class CollisionHandlerLogic extends LogicComponent {
	public CollisionHandlerLogic() {
		super();
	}

	public CollisionHandlerLogic(Aspect dependencies) {
		super(dependencies);
	}

	/**
	 * Returns if the collision should happen in the physics world
	 * 
	 * @param entity
	 * @return
	 */
	public abstract boolean handleCollision(Entity entity);
}
