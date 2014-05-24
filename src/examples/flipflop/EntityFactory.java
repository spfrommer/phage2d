package examples.flipflop;

import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Rectangle;

import utils.image.ImageUtils;
import utils.image.Texture;
import utils.physics.Vector;
import engine.core.factory.ComponentFactory;
import engine.core.framework.Entity;
import engine.core.implementation.physics.base.CollisionListener;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.logic.handler.ListenerCollisionHandlerLogic;
import engine.core.implementation.rendering.logic.TextureRenderingLogic;

public class EntityFactory {
	public static Entity makePortal(Vector position, double rotation,
			CollisionListener listener) {
		Entity portal = new Entity();

		PhysicsData physics = ComponentFactory.addPhysicsData(portal, position,
				0, new Rectangle(50, 50));
		physics.setMassType(Mass.Type.INFINITE);
		physics.setRotation(rotation);

		ComponentFactory.addTextureData(portal,
				new Texture(ImageUtils.getID("portal.png"), 50, 50));
		ComponentFactory.addNameData(portal, "platform");
		ComponentFactory.addPhysicsWrappers(portal);
		ComponentFactory.addLayerData(portal, 2);
		ListenerCollisionHandlerLogic handler = new ListenerCollisionHandlerLogic(
				portal);
		handler.addListener(listener);
		portal.addComponent(handler);
		portal.addComponent(new TextureRenderingLogic(portal));
		return portal;
	}

	public static Entity makeBall(Vector position) {
		Entity player = new Entity();

		PhysicsData physics = ComponentFactory.addPhysicsData(player, position,
				0, new Circle(20));
		physics.setGravity(100);
		physics.setCollisionFriction(0);
		physics.setRestitution(0.01);
		physics.setRotationalFriction(100);

		ComponentFactory.addTextureData(player,
				new Texture(ImageUtils.getID("blueblob.png"), 40, 40));
		ComponentFactory.addNameData(player, "player");
		ComponentFactory.addPhysicsWrappers(player);
		ComponentFactory.addLayerData(player, 2);
		player.addComponent(new TextureRenderingLogic(player));
		return player;
	}

	public static Entity makePlatform(Vector position, double width,
			double height) {
		Entity platform = new Entity();

		PhysicsData physics = ComponentFactory.addPhysicsData(platform,
				position, 0, new Rectangle(width, height));
		physics.setMassType(Mass.Type.INFINITE);

		ComponentFactory.addTextureData(platform,
				new Texture(ImageUtils.getID("darkpanel.jpg"), width, height));
		ComponentFactory.addNameData(platform, "platform");
		ComponentFactory.addPhysicsWrappers(platform);
		ComponentFactory.addLayerData(platform, 2);
		platform.addComponent(new TextureRenderingLogic(platform));
		return platform;
	}

	public static Entity makeBackground(double width, double height) {
		Entity background = new Entity();

		ComponentFactory.addShellData(background, new Vector(0, 0), 0);

		ComponentFactory.addTextureData(background,
				new Texture(ImageUtils.getID("terrain2.png"), width, height));
		ComponentFactory.addShellWrappers(background);
		ComponentFactory.addNameData(background, "background");
		ComponentFactory.addLayerData(background, 0);
		background.addComponent(new TextureRenderingLogic(background));
		return background;
	}
}
