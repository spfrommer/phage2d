package examples.flipflop.world;

import engine.core.framework.EntitySystem;
import examples.flipflop.EntityFactory;

public class World3Factory implements WorldFactory {
	@Override
	public void setWorld(EntitySystem system) {
		EntityFactory.PLATFORM_IMAGE = "minecraftblock.png";

		system.addEntity(EntityFactory.makeBackground("terrain4.jpg", 15000, 6000));
	}
}
