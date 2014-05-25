package examples.flipflop;

import java.util.ArrayList;
import java.util.List;

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
import engine.core.implementation.rendering.base.Animator;
import engine.core.implementation.rendering.data.AnimationData;
import engine.core.implementation.rendering.logic.TextureRenderingLogic;

public class EntityFactory {
	public static Entity makePortal(Vector position) {
		Entity portal = new Entity();

		PhysicsData physics = ComponentFactory.addPhysicsData(portal, position,
				0, new Circle(25));
		physics.setMassType(Mass.Type.INFINITE);
		physics.setRotationalFriction(0);
		physics.setRotationalVelocity(5);

		ComponentFactory.addTextureData(portal,
				new Texture(ImageUtils.getID("portal1.png"), 50, 50));

		AnimationData animation = new AnimationData(portal);
		List<Texture> frames = new ArrayList<Texture>();
		for (int i = 1; i <= 9; i++)
			frames.add(new Texture(ImageUtils.getID("portal" + i + ".png"), 50,
					50));
		Animator animator = new Animator(frames, 1);
		animation.addAnimator("activation", animator);
		portal.addComponent(animation);

		ComponentFactory.addNameData(portal, "portal");
		ComponentFactory.addPhysicsWrappers(portal);
		ComponentFactory.addLayerData(portal, 2);
		ListenerCollisionHandlerLogic handler = new ListenerCollisionHandlerLogic(
				portal);
		portal.addComponent(handler);
		portal.addComponent(new TextureRenderingLogic(portal));
		return portal;
	}

	public static Entity makePortal(Vector position, CollisionListener listener) {
		Entity portal = new Entity();

		PhysicsData physics = ComponentFactory.addPhysicsData(portal, position,
				0, new Rectangle(50, 50));
		physics.setMassType(Mass.Type.INFINITE);

		ComponentFactory.addTextureData(portal,
				new Texture(ImageUtils.getID("portal1.png"), 50, 50));
		ComponentFactory.addNameData(portal, "portal");
		ComponentFactory.addPhysicsWrappers(portal);
		ComponentFactory.addLayerData(portal, 2);
		ListenerCollisionHandlerLogic handler = new ListenerCollisionHandlerLogic(
				portal);
		handler.addListener(listener);
		portal.addComponent(handler);
		portal.addComponent(new TextureRenderingLogic(portal));
		return portal;
	}

	public static Entity makeScenery(String imageName, Vector position,
			double width, double height) {
		Entity background = new Entity();

		ComponentFactory.addShellData(background, position, 0);

		ComponentFactory.addTextureData(background,
				new Texture(ImageUtils.getID(imageName), width, height));
		ComponentFactory.addShellWrappers(background);
		ComponentFactory.addNameData(background, "scenery");
		ComponentFactory.addLayerData(background, 1);
		background.addComponent(new TextureRenderingLogic(background));
		return background;
	}

	public static Entity makeScenery(String imageName, Vector position,
			double width, double height, int layer) {
		Entity background = new Entity();

		ComponentFactory.addShellData(background, position, 0);

		ComponentFactory.addTextureData(background,
				new Texture(ImageUtils.getID(imageName), width, height));
		ComponentFactory.addShellWrappers(background);
		ComponentFactory.addNameData(background, "scenery");
		ComponentFactory.addLayerData(background, layer);
		background.addComponent(new TextureRenderingLogic(background));
		return background;
	}

	public static Entity makeBall(Vector position) {
		Entity player = new Entity();

		PhysicsData physics = ComponentFactory.addPhysicsData(player, position,
				0, new Circle(23));
		physics.setGravity(100);
		physics.setCollisionFriction(0);
		physics.setRestitution(0.01);
		physics.setRotationalFriction(100);

		ComponentFactory.addTextureData(player,
				new Texture(ImageUtils.getID("blueblob.png"), 46, 46));
		ComponentFactory.addNameData(player, "ball");
		ComponentFactory.addPhysicsWrappers(player);
		ComponentFactory.addLayerData(player, 2);
		player.addComponent(new TextureRenderingLogic(player));
		return player;
	}

	public static String PLATFORM_IMAGE = "";

	public static Entity makePlatform(Vector position, double width,
			double height) {
		Entity platform = new Entity();

		PhysicsData physics = ComponentFactory.addPhysicsData(platform,
				position, 0, new Rectangle(width, height));
		physics.setMassType(Mass.Type.INFINITE);
		ComponentFactory.addTextureData(platform,
				new Texture(ImageUtils.getID(PLATFORM_IMAGE), width, height));
		ComponentFactory.addNameData(platform, "platform");
		ComponentFactory.addPhysicsWrappers(platform);
		ComponentFactory.addLayerData(platform, 2);
		platform.addComponent(new TextureRenderingLogic(platform));
		return platform;
	}

	public static Entity makeBackground(String imageName, double width,
			double height) {
		Entity background = new Entity();

		ComponentFactory.addShellData(background, new Vector(0, 0), 0);

		ComponentFactory.addTextureData(background,
				new Texture(ImageUtils.getID(imageName), width, height));
		ComponentFactory.addShellWrappers(background);
		ComponentFactory.addNameData(background, "background");
		ComponentFactory.addLayerData(background, 0);
		background.addComponent(new TextureRenderingLogic(background));
		return background;
	}

	public static Entity makeBackground(String imageName, Vector position,
			double width, double height) {
		Entity background = new Entity();

		ComponentFactory.addShellData(background, position, 0);

		ComponentFactory.addTextureData(background,
				new Texture(ImageUtils.getID(imageName), width, height));
		ComponentFactory.addShellWrappers(background);
		ComponentFactory.addNameData(background, "background");
		ComponentFactory.addLayerData(background, 0);
		background.addComponent(new TextureRenderingLogic(background));
		return background;
	}
}
