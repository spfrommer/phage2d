package examples.flipflop;

import utils.physics.Vector;
import engine.core.framework.EntitySystem;

public class WorldFactory {
	public static void setWorld0(EntitySystem system) {
		EntityFactory.PLATFORM_IMAGE = "darkpanel.jpg";

		system.addEntity(EntityFactory.makeBackground("terrain2.png", 15000,
				6000));
		system.addEntity(EntityFactory.makeScenery("palmtree3.png", new Vector(
				-3000, 0), 500, 3000));
		system.addEntity(EntityFactory.makeScenery("palmtree1.png", new Vector(
				-1000, 0), 2000, 3000));
		system.addEntity(EntityFactory.makeScenery("palmtree2.png", new Vector(
				-1000, 0), 500, 3000));
		system.addEntity(EntityFactory.makeScenery("palmtree3.png", new Vector(
				0, 0), 500, 3000));
		system.addEntity(EntityFactory.makeScenery("palmtree1.png", new Vector(
				500, 0), 1500, 3000));
		system.addEntity(EntityFactory.makeScenery("palmtree3.png", new Vector(
				2000, 0), 500, 3000));
		system.addEntity(EntityFactory.makeScenery("palmtree2.png", new Vector(
				3000, 0), 500, 3000));
	}

	public static void setWorld1(EntitySystem system) {
		EntityFactory.PLATFORM_IMAGE = "sandbrick.jpeg";

		system.addEntity(EntityFactory.makeBackground("terrain3.jpg", 15000,
				6000));
		system.addEntity(EntityFactory.makeScenery("lensflare.png", new Vector(
				-400, 950), 100, 100, 0));
		system.addEntity(EntityFactory.makeScenery("lenscircle.png",
				new Vector(-250, 600), 15, 15, 1));
		system.addEntity(EntityFactory.makeScenery("lenscircle.png",
				new Vector(-200, 500), 30, 30, 2));
		system.addEntity(EntityFactory.makeScenery("lenscircle.png",
				new Vector(-200, 500), 30, 30, 3));
		system.addEntity(EntityFactory.makeScenery("lenscircle.png",
				new Vector(-200, 500), 50, 50, 4));
	}

	public static void setWorld2(EntitySystem system) {
		EntityFactory.PLATFORM_IMAGE = "minecraftblock.png";

		system.addEntity(EntityFactory.makeBackground("terrain4.jpg", 15000,
				6000));
	}

	public static void setWorld3(EntitySystem system) {
		EntityFactory.PLATFORM_IMAGE = "marbleblock.jpg";

		system.addEntity(EntityFactory.makeBackground("terrain5.jpg",
				new Vector(0, 1000), 15000, 6000));

		system.addEntity(EntityFactory.makeScenery("statue3.png", new Vector(
				-1500, -500), 500, 1000));
		system.addEntity(EntityFactory.makeScenery("statue1.png", new Vector(
				-1000, -500), 500, 1000));
		system.addEntity(EntityFactory.makeScenery("statue2.png", new Vector(
				-500, -500), 500, 1000));
		system.addEntity(EntityFactory.makeScenery("statue3.png", new Vector(0,
				-500), 500, 1000));
		system.addEntity(EntityFactory.makeScenery("statue1.png", new Vector(
				500, -500), 500, 1000));
		system.addEntity(EntityFactory.makeScenery("statue3.png", new Vector(
				1000, -500), 500, 1000));
		system.addEntity(EntityFactory.makeScenery("statue2.png", new Vector(
				1500, -500), 500, 1000));
	}

	public static void setWorld4(EntitySystem system) {
		EntityFactory.PLATFORM_IMAGE = "marbleblock.jpg";

		system.addEntity(EntityFactory.makeBackground("terrain5.jpg",
				new Vector(0, 1000), 15000, 6000));
	}
}
