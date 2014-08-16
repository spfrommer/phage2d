package examples.flipflop.world;

import utils.physics.Vector;
import engine.core.framework.EntitySystem;
import examples.flipflop.EntityFactory;

public class World1Factory implements WorldFactory {
	@Override
	public void setWorld(EntitySystem system) {
		EntityFactory.PLATFORM_IMAGE = "darkpanel.jpg";

		system.addEntity(EntityFactory.makeBackground("terrain2.png", 15000, 6000));
		system.addEntity(EntityFactory.makeScenery("palmtree3.png", new Vector(-3000, 0), 500, 3000));
		system.addEntity(EntityFactory.makeScenery("palmtree1.png", new Vector(-1000, 0), 2000, 3000));
		system.addEntity(EntityFactory.makeScenery("palmtree2.png", new Vector(-1000, 0), 500, 3000));
		system.addEntity(EntityFactory.makeScenery("palmtree3.png", new Vector(0, 0), 500, 3000));
		system.addEntity(EntityFactory.makeScenery("palmtree1.png", new Vector(500, 0), 1500, 3000));
		system.addEntity(EntityFactory.makeScenery("palmtree3.png", new Vector(2000, 0), 500, 3000));
		system.addEntity(EntityFactory.makeScenery("palmtree2.png", new Vector(3000, 0), 500, 3000));
	}
}
