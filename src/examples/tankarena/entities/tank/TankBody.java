package examples.tankarena.entities.tank;

import org.dyn4j.geometry.Rectangle;

import utils.image.ImageUtils;
import utils.image.Texture;
import utils.physics.Vector;
import engine.core.factory.ComponentFactory;
import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.camera.data.CameraFocusData;
import engine.core.implementation.network.base.encoding.BasicDataEncoder;
import engine.core.implementation.network.logic.ServerLogic;
import engine.core.implementation.network.wrappers.EncoderWrapper;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.wrappers.PhysicsTransformWrapper;
import engine.core.implementation.rendering.logic.TextureRenderingLogic;
import engine.inputs.InputManager;

/**
 * An Entity that represents the body of a Tank.
 */
public class TankBody extends Entity {
	/**
	 * Makes a TankBody for a player.
	 * 
	 * @param position
	 * @param center
	 * @param width
	 * @param height
	 * @param texture
	 * @param layer
	 * @param focusID
	 * @param input
	 */
	public TankBody(Vector position, Vector center, double width, double height, String texture, double layer,
			int focusID, InputManager input) {
		super();

		PhysicsData physics = ComponentFactory.addPhysicsData(this, position, 0, new Rectangle(width, height));
		// Add large friction so that tank essentially stops when not moved
		physics.setRotationalFriction(10);
		physics.setMovementFriction(10);
		physics.setCenter(center);
		physics.setMass(100);
		physics.setVelocity(new Vector(0, 100));
		physics.setRotationalVelocity(1);

		ComponentFactory.addTextureData(this, new Texture(ImageUtils.getID(texture), width, height));

		ComponentFactory.addNetworkData(this);
		ComponentFactory.addHealthData(this, 100);
		ComponentFactory.addNameData(this, "tankbody");
		ComponentFactory.addLayerData(this, layer);
		ComponentFactory.addCameraFocusData(this, focusID);
		this.addComponent(new PhysicsTransformWrapper());

		EncoderWrapper encoder = ComponentFactory.createBasicEncoder(this);
		encoder.addDataEncoder(TypeManager.getType(CameraFocusData.class), new BasicDataEncoder());
		this.addComponent(encoder);

		// for single player mode
		this.addComponent(new TextureRenderingLogic());

		this.addComponent(new ServerLogic());
	}
}