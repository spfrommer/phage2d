package examples.tankarena.entities.tank.body;

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
import examples.tankarena.entities.tank.ComponentDamageHandlerLogic;
import examples.tankarena.entities.tank.Tank;
import examples.tankarena.entities.tank.TankComponent;

/**
 * An Entity that represents the body of a Tank.
 */
public class TankBody extends Entity implements TankComponent {
	private Tank m_tank;

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
	TankBody(Vector position, Vector center, double width, double height, String texture, double layer, int focusID) {
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
		ComponentFactory.addNameData(this, "tankbody");
		ComponentFactory.addLayerData(this, layer);
		ComponentFactory.addCameraFocusData(this, focusID);
		this.addComponent(new PhysicsTransformWrapper());

		EncoderWrapper encoder = ComponentFactory.createBasicEncoder(this);
		encoder.addDataEncoder(TypeManager.getType(CameraFocusData.class), new BasicDataEncoder());
		this.addComponent(encoder);

		ComponentDamageHandlerLogic damageHandler = new ComponentDamageHandlerLogic();
		this.addComponent(damageHandler);

		// for single player mode
		this.addComponent(new TextureRenderingLogic());

		this.addComponent(new ServerLogic());
	}

	/**
	 * Sets the Tank this component belongs to.
	 * 
	 * @param tank
	 */
	@Override
	public void setTank(Tank tank) {
		m_tank = tank;

		ComponentDamageHandlerLogic damageHandler = (ComponentDamageHandlerLogic) this.getComponent(TypeManager
				.getType(ComponentDamageHandlerLogic.class));
		damageHandler.setTank(m_tank);
	}

	/**
	 * Gets the Tank this component belongs to.
	 * 
	 * @return
	 */
	@Override
	public Tank getTank() {
		return m_tank;
	}

	public static class BodyBuilder {
		Vector position;
		Vector center;
		double width;
		double height;
		String texture;
		double layer;
		int focusID;

		public TankBody build() {
			return new TankBody(position, center, width, height, texture, layer, focusID);
		}

		/**
		 * @param position
		 *            the position to set
		 */
		public void setPosition(Vector position) {
			this.position = position;
		}

		/**
		 * @param center
		 *            the center to set
		 */
		public void setCenter(Vector center) {
			this.center = center;
		}

		/**
		 * @param width
		 *            the width to set
		 */
		public void setWidth(double width) {
			this.width = width;
		}

		/**
		 * @param height
		 *            the height to set
		 */
		public void setHeight(double height) {
			this.height = height;
		}

		/**
		 * @param texture
		 *            the texture to set
		 */
		public void setTexture(String texture) {
			this.texture = texture;
		}

		/**
		 * @param layer
		 *            the layer to set
		 */
		public void setLayer(int layer) {
			this.layer = layer;
		}

		/**
		 * @param focusID
		 *            the id the camera should focus on
		 */
		public void setFocusID(int focusID) {
			this.focusID = focusID;
		}
	}
}