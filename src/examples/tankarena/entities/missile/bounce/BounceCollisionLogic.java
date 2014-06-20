package examples.tankarena.entities.missile.bounce;

import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.logic.handler.CollisionHandlerLogic;

/**
 * Collides a certain number of times, then doesn't.
 * 
 * @eng.dependencies BounceData
 */
public class BounceCollisionLogic extends CollisionHandlerLogic {
	private BounceData m_bounce;

	public BounceCollisionLogic() {
		super();
	}

	@Override
	public boolean handleCollision(Entity entity) {
		m_bounce.bouncedAgainst = entity;
		m_bounce.bounceCount++;
		if (m_bounce.bounceCount >= m_bounce.maxBounces) {
			return false;
		}
		return true;
	}

	@Override
	public void loadDependencies() {
		m_bounce = (BounceData) this.loadDependency(TypeManager.getType(BounceData.class));
	}

	@Override
	public Component copy() {
		return new BounceCollisionLogic();
	}
}
