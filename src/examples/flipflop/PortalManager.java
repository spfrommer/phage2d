package examples.flipflop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.core.framework.Entity;
import engine.core.implementation.physics.base.CollisionListener;

public class PortalManager implements CollisionListener {

	private Map<Entity, Boolean> m_portals = new HashMap<Entity, Boolean>();

	private List<PortalsSatisfiedListener> m_portalListeners = new ArrayList<PortalsSatisfiedListener>();

	public void addPortalsSatisfiedListener(PortalsSatisfiedListener listener) {
		m_portalListeners.add(listener);
	}

	public void removePortalsSatisfiedListener(PortalsSatisfiedListener listener) {
		m_portalListeners.remove(listener);
	}

	public void addPortal(Entity portal) {
		m_portals.put(portal, false);
	}

	public void resetPortals() {
		m_portals.clear();
	}

	public boolean portalsSatisfied() {
		for (Entity e : m_portals.keySet()) {
			if (!m_portals.get(e))
				return false;
		}
		return true;
	}

	@Override
	public void collided(Entity portal, Entity entity) {
		if (m_portals.containsKey(portal)) {
			m_portals.put(portal, true);
		}
		if (portalsSatisfied()) {
			for (PortalsSatisfiedListener listener : m_portalListeners)
				listener.portalsSatisfied();
		}
	}
}
