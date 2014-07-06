package examples.flipflop.level;

import java.util.ArrayList;
import java.util.List;

import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.NameData;
import examples.flipflop.FlipFlop;
import examples.flipflop.PortalManager;

/**
 * Copies the Entities in a world into a Level.
 */
public class MirrorLevel implements Level {
	private List<Entity> m_balls;
	private List<Entity> m_portals;
	private List<Entity> m_platforms;

	public MirrorLevel() {

	}

	public void load(FlipFlop world) {
		m_balls = new ArrayList<Entity>();
		m_portals = new ArrayList<Entity>();
		m_platforms = new ArrayList<Entity>();

		for (Entity entity : world.getEntities()) {
			NameData name = (NameData) entity.getComponent(TypeManager
					.typeOf(NameData.class));
			if (name.name.equals("ball")) {
				m_balls.add(entity);
			} else if (name.name.equals("platform")) {
				m_platforms.add(entity);
			} else if (name.name.equals("portal")) {
				m_portals.add(entity);
			}
		}
	}

	@Override
	public List<Entity> getBalls() {
		return m_balls;
	}

	@Override
	public List<Entity> getPortals(PortalManager manager) {
		return m_portals;
	}

	@Override
	public List<Entity> getPlatforms() {
		return m_platforms;
	}

}
