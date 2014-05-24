package examples.flipflop;

import java.util.List;

import engine.core.framework.Entity;

public interface Level {
	public List<Entity> getBalls();

	public List<Entity> getPortals(PortalManager manager);

	public List<Entity> getPlatforms();
}
