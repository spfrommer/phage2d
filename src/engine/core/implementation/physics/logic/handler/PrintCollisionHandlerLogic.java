package engine.core.implementation.physics.logic.handler;

import engine.core.framework.Entity;
import engine.core.framework.component.Component;

/**
 * Simply prints a debug statement upon collision.
 */
public class PrintCollisionHandlerLogic extends CollisionHandlerLogic {
	public PrintCollisionHandlerLogic() {
		super();
	}

	public PrintCollisionHandlerLogic(Entity parent) {
		super(parent);
	}

	@Override
	public boolean handleCollision(Entity entity) {
		System.out.println("Collided with: " + entity);
		return true;
	}

	@Override
	public void loadDependencies() {

	}

	@Override
	public Component copy() {
		return new PrintCollisionHandlerLogic();
	}
}
