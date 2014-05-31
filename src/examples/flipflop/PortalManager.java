package examples.flipflop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.base.CollisionListener;
import engine.core.implementation.rendering.data.AnimationData;
import engine.core.implementation.rendering.data.TextureData;

public class PortalManager implements CollisionListener {
	private EntitySystem m_system;

	private Map<Entity, Boolean> m_portals;

	private List<PortalsSatisfiedListener> m_portalListeners;

	{
		m_portals = new HashMap<Entity, Boolean>();
		m_portalListeners = new ArrayList<PortalsSatisfiedListener>();
	}

	public PortalManager(EntitySystem system) {
		m_system = system;
	}

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
		AnimationData animation = (AnimationData) portal.getComponent(TypeManager.getType(AnimationData.class));
		TextureData texture = (TextureData) portal.getComponent(TypeManager.getType(TextureData.class));
		animation.getAnimator("activation").animate(texture.texture);

		if (m_portals.containsKey(portal)) {
			m_portals.put(portal, true);
			m_system.removeEntity(entity);
		}
		if (portalsSatisfied()) {
			for (PortalsSatisfiedListener listener : m_portalListeners)
				listener.portalsSatisfied();
		}
	}
}
