package engine.core.factory;

import org.dyn4j.geometry.Convex;

import utils.image.Texture;
import utils.physics.Vector;
import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.NameData;
import engine.core.implementation.camera.data.CameraFocusData;
import engine.core.implementation.extras.data.DamageData;
import engine.core.implementation.extras.data.HealthData;
import engine.core.implementation.network.base.encoding.BasicDataEncoder;
import engine.core.implementation.network.base.encoding.BasicDependentEncoder;
import engine.core.implementation.network.data.NetworkData;
import engine.core.implementation.network.data.NetworkEncoder;
import engine.core.implementation.network.logic.ClientLogic;
import engine.core.implementation.network.wrappers.EncoderWrapper;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.data.PhysicsShellData;
import engine.core.implementation.physics.data.PhysicsToShellEncoder;
import engine.core.implementation.physics.data.ShellEncoder;
import engine.core.implementation.physics.wrappers.physics.PhysicsCenterWrapper;
import engine.core.implementation.physics.wrappers.physics.PhysicsPositionWrapper;
import engine.core.implementation.physics.wrappers.physics.PhysicsRotationWrapper;
import engine.core.implementation.physics.wrappers.physics.PhysicsVelocityWrapper;
import engine.core.implementation.physics.wrappers.shell.ShellCenterWrapper;
import engine.core.implementation.physics.wrappers.shell.ShellPositionWrapper;
import engine.core.implementation.physics.wrappers.shell.ShellRotationWrapper;
import engine.core.implementation.physics.wrappers.shell.ShellVelocityWrapper;
import engine.core.implementation.rendering.data.LayerData;
import engine.core.implementation.rendering.data.TextureData;
import engine.core.implementation.rendering.data.TextureEncoder;
import engine.core.implementation.rendering.logic.TextureRenderingLogic;

/**
 * A factory for adding Data and Wrapper components easily to an Entity
 */
public class ComponentFactory {
	/**
	 * Adds a PhysicsData to the Entity, then returns the PhysicsData
	 * 
	 * @param entity
	 * @param position
	 * @param rotation
	 * @param convex
	 * @param width
	 * @param height
	 * @return
	 */
	public static PhysicsData addPhysicsData(Entity entity, Vector position, double rotation, Convex convex) {
		PhysicsData physics = new PhysicsData(entity, convex);
		physics.setPosition(position);
		physics.setRotation(rotation);
		entity.addComponent(physics);
		return physics;
	}

	/**
	 * Adds a PhysicsShellData to the Entity with center and velocity initialized to (0, 0) and position and rotation
	 * initialized to the parameters
	 * 
	 * @param entity
	 * @param position
	 * @param rotation
	 * @return
	 */
	public static PhysicsShellData addShellData(Entity entity, Vector position, double rotation) {
		PhysicsShellData shell = new PhysicsShellData(entity);
		shell.center = new Vector(0, 0);
		shell.velocity = new Vector(0, 0);
		shell.position = position;
		shell.rotation = rotation;
		entity.addComponent(shell);
		return shell;
	}

	/**
	 * Adds a TextureData to the Entity, then returns the TextureData
	 * 
	 * @param entity
	 * @param texture
	 * @return
	 */
	public static TextureData addTextureData(Entity entity, Texture texture) {
		TextureData textureData = new TextureData(entity);
		textureData.texture = texture;
		entity.addComponent(textureData);
		return textureData;
	}

	/**
	 * Adds a LayerData to the Entity, then returns the LayerData
	 * 
	 * @param entity
	 * @param layer
	 * @return
	 */
	public static LayerData addLayerData(Entity entity, int layer) {
		LayerData layerData = new LayerData(entity);
		layerData.layer = layer;
		entity.addComponent(layerData);
		return layerData;
	}

	/**
	 * Adds a NetworkData to the Entity, then returns the NetworkData
	 * 
	 * @param entity
	 * @return
	 */
	public static NetworkData addNetworkData(Entity entity) {
		NetworkData network = new NetworkData(entity);
		entity.addComponent(network);
		return network;
	}

	/**
	 * Adds a HealthData to the Entity, then returns the HealthData
	 * 
	 * @param entity
	 * @param health
	 * 
	 * @return
	 */
	public static HealthData addHealthData(Entity entity, double health) {
		HealthData healthData = new HealthData(entity);
		healthData.health = health;
		entity.addComponent(healthData);
		return healthData;
	}

	/**
	 * Adds a DamageData to the Entity, then returns the DamageData
	 * 
	 * @param entity
	 * @param damage
	 * @return
	 */
	public static DamageData addDamageData(Entity entity, double damage) {
		DamageData damageData = new DamageData(entity);
		damageData.damage = damage;
		entity.addComponent(damageData);
		return damageData;
	}

	/**
	 * Adds a CameraFocusData to the Entity, then returns the CameraFocusData
	 * 
	 * @param entity
	 * @param focusID
	 * @return
	 */
	public static CameraFocusData addCameraFocusData(Entity entity, int focusID) {
		CameraFocusData focus = new CameraFocusData(entity);
		focus.cameraID = focusID;
		entity.addComponent(focus);
		return focus;
	}

	/**
	 * Adds a NameData to the Entity
	 * 
	 * @param entity
	 * @param name
	 * @return
	 */
	public static NameData addNameData(Entity entity, String name) {
		NameData nameData = new NameData(entity);
		nameData.name = name;
		entity.addComponent(nameData);
		return nameData;
	}

	/**
	 * Adds a Position, Rotation, Velocity, and Center PhysicsWrappers to this Entity - used mainly for rendering single
	 * player games or for the ServerLogic to transmit updates to the client
	 * 
	 * @param entity
	 */
	public static void addPhysicsWrappers(Entity entity) {
		entity.addComponent(new PhysicsPositionWrapper(entity));
		entity.addComponent(new PhysicsRotationWrapper(entity));
		entity.addComponent(new PhysicsVelocityWrapper(entity));
		entity.addComponent(new PhysicsCenterWrapper(entity));
	}

	/**
	 * Adds a Position, Rotation, Velocity, and Center ShellWrappers to this Entity
	 * 
	 * @param entity
	 */
	public static void addShellWrappers(Entity entity) {
		entity.addComponent(new ShellPositionWrapper(entity));
		entity.addComponent(new ShellRotationWrapper(entity));
		entity.addComponent(new ShellVelocityWrapper(entity));
		entity.addComponent(new ShellCenterWrapper(entity));
	}

	/**
	 * Adds all the encoders to a server-side entity that it will need to get a client terminal to render an entity to
	 * an EncoderWrapper, which it DOES NOTE ADD to the Entity then returns. Works with either physics or shell data.
	 * 
	 * @param entity
	 * @return
	 */
	public static EncoderWrapper createBasicEncoder(Entity entity) {
		EncoderWrapper encoder = new EncoderWrapper(entity);
		encoder.addDataEncoder(TypeManager.getType(PhysicsData.class), new PhysicsToShellEncoder());
		encoder.addDataEncoder(TypeManager.getType(PhysicsShellData.class), new ShellEncoder());
		encoder.addDataEncoder(TypeManager.getType(TextureData.class), new TextureEncoder());
		encoder.addDataEncoder(TypeManager.getType(LayerData.class), new BasicDataEncoder());
		encoder.addDataEncoder(TypeManager.getType(NetworkData.class), new NetworkEncoder(false));
		encoder.addDependentEncoder(new BasicDependentEncoder(ShellPositionWrapper.class));
		encoder.addDependentEncoder(new BasicDependentEncoder(ShellRotationWrapper.class));
		encoder.addDependentEncoder(new BasicDependentEncoder(ShellCenterWrapper.class));
		encoder.addDependentEncoder(new BasicDependentEncoder(ClientLogic.class));
		encoder.addDependentEncoder(new BasicDependentEncoder(TextureRenderingLogic.class));

		return encoder;
	}
}
