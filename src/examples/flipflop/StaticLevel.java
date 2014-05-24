package examples.flipflop;

import java.util.ArrayList;
import java.util.List;

import utils.physics.Vector;
import engine.core.framework.Entity;

public class StaticLevel implements Level {
	public List<Entity> getBalls() {
		ArrayList<Entity> balls = new ArrayList<Entity>();
		balls.add(EntityFactory.makeBall(new Vector(-100, 0)));
		balls.add(EntityFactory.makeBall(new Vector(0, 0)));
		balls.add(EntityFactory.makeBall(new Vector(100, 0)));
		return balls;
	}

	public List<Entity> getPortals(PortalManager portalManager) {
		ArrayList<Entity> portals = new ArrayList<Entity>();
		portals.add(EntityFactory.makePortal(new Vector(150, -300), 0,
				portalManager));
		portals.add(EntityFactory.makePortal(new Vector(200, -300), 0,
				portalManager));
		portals.add(EntityFactory.makePortal(new Vector(250, -300), 0,
				portalManager));
		return portals;
	}

	public List<Entity> getPlatforms() {
		ArrayList<Entity> platforms = new ArrayList<Entity>();

		addSquarePlatform(new Vector(-100, -100), platforms);
		addSquarePlatform(new Vector(0, -150), platforms);
		addSquarePlatform(new Vector(100, -200), platforms);

		addSquarePlatform(new Vector(200, -50), platforms);
		addSquarePlatform(new Vector(250, -100), platforms);
		addSquarePlatform(new Vector(300, -150), platforms);

		return platforms;
	}

	private void addSquarePlatform(Vector position, List<Entity> entities) {
		entities.add(EntityFactory.makePlatform(position, 50, 50));
	}
}
