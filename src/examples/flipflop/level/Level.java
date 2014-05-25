package examples.flipflop.level;

import java.util.List;

import engine.core.framework.Entity;
import examples.flipflop.PortalManager;

public interface Level {
	public List<Entity> getBalls();

	public List<Entity> getPortals(PortalManager manager);

	public List<Entity> getPlatforms();
}
