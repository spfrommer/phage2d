package engine.core.implementation.physics.logic.handler;

import java.util.ArrayList;
import java.util.List;

import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.implementation.physics.base.CollisionListener;

public class ListenerCollisionHandlerLogic extends CollisionHandlerLogic {
	private List<CollisionListener> m_listeners;

	{
		m_listeners = new ArrayList<CollisionListener>();
	}

	public ListenerCollisionHandlerLogic() {

	}

	public void addListener(CollisionListener listener) {
		m_listeners.add(listener);
	}

	public void removeListener(CollisionListener listener) {
		m_listeners.remove(listener);
	}

	@Override
	public boolean handleCollision(Entity entity) {
		for (CollisionListener listener : m_listeners)
			listener.collided(this.getEntity(), entity);
		return true;
	}

	@Override
	public void loadDependencies() {

	}

	@Override
	public Component copy() {
		return new ListenerCollisionHandlerLogic();
	}
}
