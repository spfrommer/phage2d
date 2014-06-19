package engine.core.implementation.physics.logic.filter;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.LogicComponent;

/**
 * Filters collisions based upon subclass logic.
 */
public abstract class CollisionFilterLogic extends LogicComponent {
	public CollisionFilterLogic() {
		super();
	}

	public CollisionFilterLogic(Aspect dependencies) {
		super(dependencies);
	}

	public abstract boolean allowCollision(Entity entity);
}
