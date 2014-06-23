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
import engine.core.implementation.interpolation.data.InterpolationData;
import engine.core.implementation.network.base.encoding.BasicBlankEncoder;
import engine.core.implementation.network.base.encoding.BasicDataEncoder;
import engine.core.implementation.network.data.NetworkData;
import engine.core.implementation.network.data.NetworkEncoder;
import engine.core.implementation.network.logic.ClientLogic;
import engine.core.implementation.network.wrappers.EncoderWrapper;
import engine.core.implementation.physics.base.PhysicsToShellEncoder;
import engine.core.implementation.physics.base.ShellEncoder;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.data.PhysicsShellData;
import engine.core.implementation.physics.wrappers.ShellTransformWrapper;
import engine.core.implementation.rendering.data.LayerData;
import engine.core.implementation.rendering.data.TextureData;
import engine.core.implementation.rendering.data.TextureEncoder;
import engine.core.implementation.rendering.logic.TextureRenderingLogic;

/**
 * A factory for adding Data and Wrapper components easily to an Entity
 */
public class ComponentFactory {
	private ComponentFactory() {

	}

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
		PhysicsData physics = new PhysicsData(convex);
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
		PhysicsShellData shell = new PhysicsShellData();
		shell.center = new Vector(0, 0);
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
		TextureData textureData = new TextureData();
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
	public static LayerData addLayerData(Entity entity, double layer) {
		LayerData layerData = new LayerData();
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
		NetworkData network = new NetworkData();
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
		HealthData healthData = new HealthData();
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
		DamageData damageData = new DamageData();
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
		CameraFocusData focus = new CameraFocusData();
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
		NameData nameData = new NameData();
		nameData.name = name;
		entity.addComponent(nameData);
		return nameData;
	}

	/**
	 * Adds all the encoders to a server-side entity that it will need to get a client terminal to render an entity to
	 * an EncoderWrapper, which it DOES NOTE ADD to the Entity then returns. Works with either physics or shell data.
	 * Includes Interpolation Data.
	 * 
	 * @param entity
	 * @param interpolation
	 * @return
	 */
	public static EncoderWrapper createBasicEncoder(Entity entity) {
		EncoderWrapper encoder = new EncoderWrapper();
		encoder.addDataEncoder(TypeManager.getType(PhysicsData.class), new PhysicsToShellEncoder());
		encoder.addDataEncoder(TypeManager.getType(PhysicsShellData.class), new ShellEncoder());
		encoder.addDataEncoder(TypeManager.getType(TextureData.class), new TextureEncoder());
		encoder.addDataEncoder(TypeManager.getType(LayerData.class), new BasicDataEncoder());
		encoder.addDataEncoder(TypeManager.getType(NetworkData.class), new NetworkEncoder(false));
		encoder.addDataEncoder(TypeManager.getType(NameData.class), new BasicDataEncoder());
		encoder.addBlankEncoder(new BasicBlankEncoder(InterpolationData.class));
		encoder.addBlankEncoder(new BasicBlankEncoder(ShellTransformWrapper.class));
		encoder.addBlankEncoder(new BasicBlankEncoder(ClientLogic.class));
		encoder.addBlankEncoder(new BasicBlankEncoder(TextureRenderingLogic.class));
		return encoder;
	}
}
