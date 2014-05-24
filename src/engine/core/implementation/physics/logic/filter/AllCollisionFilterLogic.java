package engine.core.implementation.physics.logic.filter;

import engine.core.framework.Entity;
import engine.core.framework.component.Component;

/**
 * Allows all collisions to occur.
 */
public class AllCollisionFilterLogic extends CollisionFilterLogic {
	public AllCollisionFilterLogic() {
		super();
	}

	public AllCollisionFilterLogic(Entity parent) {
		super(parent);
	}

	@Override
	public boolean allowCollision(Entity parent) {
		return true;
	}

	@Override
	public void loadDependencies() {

	}

	@Override
	public Component copy() {
		return new AllCollisionFilterLogic();
	}
}
