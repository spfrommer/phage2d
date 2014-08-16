package examples.flipflop.world;

import utils.physics.Vector;
import engine.core.framework.EntitySystem;
import examples.flipflop.EntityFactory;

public class World4Factory implements WorldFactory {
	@Override
	public void setWorld(EntitySystem system) {
		EntityFactory.PLATFORM_IMAGE = "marbleblock.jpg";

		system.addEntity(EntityFactory.makeBackground("terrain5.jpg", new Vector(0, 1000), 15000, 6000));

		system.addEntity(EntityFactory.makeScenery("statue3.png", new Vector(-1500, -500), 500, 1000));
		system.addEntity(EntityFactory.makeScenery("statue1.png", new Vector(-1000, -500), 500, 1000));
		system.addEntity(EntityFactory.makeScenery("statue2.png", new Vector(-500, -500), 500, 1000));
		system.addEntity(EntityFactory.makeScenery("statue3.png", new Vector(0, -500), 500, 1000));
		system.addEntity(EntityFactory.makeScenery("statue1.png", new Vector(500, -500), 500, 1000));
		system.addEntity(EntityFactory.makeScenery("statue3.png", new Vector(1000, -500), 500, 1000));
		system.addEntity(EntityFactory.makeScenery("statue2.png", new Vector(1500, -500), 500, 1000));
	}
}
