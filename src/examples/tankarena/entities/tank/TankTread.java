package examples.tankarena.entities.tank;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Rectangle;

import utils.image.ImageUtils;
import utils.image.Texture;
import utils.physics.JointType;
import utils.physics.Vector;
import engine.core.factory.ComponentFactory;
import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.leaf.action.executor.ActionExecutorLeaf;
import engine.core.implementation.behavior.logic.TreeLogic;
import engine.core.implementation.network.logic.ServerLogic;
import engine.core.implementation.physics.activities.PhysicsActivity;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.wrappers.PhysicsTransformWrapper;
import engine.core.implementation.rendering.base.Animator;
import engine.core.implementation.rendering.data.AnimationData;
import engine.core.implementation.rendering.logic.TextureRenderingLogic;
import engine.inputs.InputManager;

public class TankTread extends Entity {
	public TankTread(Vector position, double width, double height, String[] textures, int layer, InputManager input,
			TankBody body, PhysicsActivity physicsActivity, int direction) {
		super();

		PhysicsData tankPhysics = (PhysicsData) body.getComponent(TypeManager.getType(PhysicsData.class));

		PhysicsData physics = ComponentFactory.addPhysicsData(this, position, 0, new Rectangle(width, height));
		physics.setRotationalFriction(0);
		physics.setMovementFriction(0);
		physics.setMass(50);

		Joint weld = PhysicsData.JointFactory.createJoint(tankPhysics, physics, position, JointType.WELD);
		physicsActivity.addJoint(weld);

		ComponentFactory.addTextureData(this, new Texture(ImageUtils.getID(textures[0]), width, height));

		List<Texture> textureForward = new ArrayList<Texture>();
		for (String frame : textures) {
			textureForward.add(new Texture(ImageUtils.getID(frame), width, height));
		}

		List<Texture> textureBackward = new ArrayList<Texture>();
		for (int i = textures.length - 1; i >= 0; i--) {
			textureBackward.add(new Texture(ImageUtils.getID(textures[i]), width, height));
		}

		AnimationData animation = new AnimationData();
		animation.addAnimator("treadforward", new Animator(textureForward, 3));
		animation.addAnimator("treadbackward", new Animator(textureBackward, 3));
		this.addComponent(animation);

		ComponentFactory.addNetworkData(this);
		ComponentFactory.addNameData(this, "tanktread");
		ComponentFactory.addLayerData(this, layer);
		this.addComponent(new PhysicsTransformWrapper());

		this.addComponent(ComponentFactory.createBasicEncoder(this));
		this.addComponent(new ServerLogic());

		// for single player mode
		this.addComponent(new TextureRenderingLogic());

		this.addComponent(new PlayerTreadLogic(input, direction));

		TreeLogic tree = new TreeLogic();
		tree.setRoot(new ActionExecutorLeaf<PlayerTreadLogic>(PlayerTreadLogic.class));
		this.addComponent(tree);
	}

	public class TreadBuilder {
		Vector position;
		double width;
		double height;
		String[] textures;
		int layer;
		InputManager input;
		TankBody body;
		PhysicsActivity activity;
		int direction;

		public TankTread build() {
			return new TankTread(position, width, height, textures, layer, input, body, activity, direction);
		}

		/**
		 * @param position
		 *            the position to set
		 */
		public void setPosition(Vector position) {
			this.position = position;
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
		 * @param textures
		 *            the textures to set
		 */
		public void setTextures(String[] textures) {
			this.textures = textures;
		}

		/**
		 * @param layer
		 *            the layer to set
		 */
		public void setLayer(int layer) {
			this.layer = layer;
		}

		/**
		 * @param input
		 *            the input to set
		 */
		public void setInputManager(InputManager input) {
			this.input = input;
		}

		/**
		 * @param body
		 *            the body to set
		 */
		public void setBody(TankBody body) {
			this.body = body;
		}

		/**
		 * @param activity
		 *            the activity to set
		 */
		public void setPhysicsActivity(PhysicsActivity activity) {
			this.activity = activity;
		}

		/**
		 * @param direction
		 *            the direction to set
		 */
		public void setDirection(int direction) {
			this.direction = direction;
		}
	}
}
