package examples.flipflop.world;

import utils.physics.Vector;
import engine.core.framework.EntitySystem;
import examples.flipflop.EntityFactory;

public class World5Factory implements WorldFactory {
	@Override
	public void setWorld(EntitySystem system) {
		EntityFactory.PLATFORM_IMAGE = "cloudblock.png";

		system.addEntity(EntityFactory.makeBackground("terrain6.jpg", new Vector(0, 1000), 15000, 6000));
	}
}
