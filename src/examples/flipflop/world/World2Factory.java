package examples.flipflop.world;

import utils.physics.Vector;
import engine.core.framework.EntitySystem;
import examples.flipflop.EntityFactory;

public class World2Factory implements WorldFactory {
	@Override
	public void setWorld(EntitySystem system) {
		EntityFactory.PLATFORM_IMAGE = "sandbrick.jpeg";

		system.addEntity(EntityFactory.makeBackground("terrain3.jpg", 15000, 6000));
		system.addEntity(EntityFactory.makeScenery("lensflare.png", new Vector(-400, 950), 100, 100, 0));
		system.addEntity(EntityFactory.makeScenery("lenscircle.png", new Vector(-250, 600), 15, 15, 1));
		system.addEntity(EntityFactory.makeScenery("lenscircle.png", new Vector(-200, 500), 30, 30, 2));
		system.addEntity(EntityFactory.makeScenery("lenscircle.png", new Vector(-200, 500), 30, 30, 3));
		system.addEntity(EntityFactory.makeScenery("lenscircle.png", new Vector(-200, 500), 50, 50, 4));
	}
}
