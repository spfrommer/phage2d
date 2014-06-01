package engine.core.implementation.physics.logic.filter;

import engine.core.framework.Entity;
import engine.core.framework.component.Component;

public class NoneCollisionFilterLogic extends CollisionFilterLogic {
	public NoneCollisionFilterLogic() {
		super();
	}

	public NoneCollisionFilterLogic(Entity parent) {
		super(parent);
	}

	@Override
	public boolean allowCollision(Entity entity) {
		return false;
	}

	@Override
	public void loadDependencies() {

	}

	@Override
	public Component copy() {
		return new NoneCollisionFilterLogic();
	}

}
