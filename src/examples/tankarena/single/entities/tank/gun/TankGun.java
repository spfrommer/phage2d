package examples.tankarena.single.entities.tank.gun;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Rectangle;

import utils.image.ImageUtils;
import utils.image.Texture;
import utils.physics.JointType;
import engine.core.factory.ComponentFactory;
import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.composite.ParallelComposite;
import engine.core.implementation.behavior.base.composite.SequencerComposite;
import engine.core.implementation.behavior.base.leaf.action.executor.ActionExecutorLeaf;
import engine.core.implementation.behavior.logic.TreeLogic;
import engine.core.implementation.physics.activities.PhysicsActivity;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.logic.filter.NoneCollisionFilterLogic;
import engine.core.implementation.physics.wrappers.PhysicsTransformWrapper;
import engine.core.implementation.rendering.base.Animator;
import engine.core.implementation.rendering.data.AnimationData;
import engine.core.implementation.rendering.logic.TextureRenderingLogic;
import engine.inputs.InputManager;
import examples.tankarena.single.entities.missile.bounce.BouncyMissile;
import examples.tankarena.single.entities.tank.Tank;
import examples.tankarena.single.entities.tank.TankComponent;
import examples.tankarena.single.entities.tank.body.TankBody;

public class TankGun extends Entity implements TankComponent {
	private Tank m_tank;

	TankGun(double width, double height, String[] textures, double layer, InputManager input, TankBody body,
			PhysicsActivity physicsActivity) {
		super();

		PhysicsData tankPhysics = (PhysicsData) body.getComponent(TypeManager.typeOf(PhysicsData.class));

		PhysicsData physics = ComponentFactory.addPhysicsData(this, tankPhysics.getPosition(), 0, new Rectangle(width,
				height));
		physics.setRotationalFriction(0);
		physics.setMovementFriction(0);
		physics.setMass(20);

		this.addComponent(new NoneCollisionFilterLogic());

		Joint weld = PhysicsData.JointFactory.createJoint(tankPhysics, physics, tankPhysics.getPosition().clone(),
				JointType.REVOLUTE);
		physicsActivity.addJoint(weld);

		ComponentFactory.addTextureData(this, new Texture(ImageUtils.getID(textures[0]), width, height));

		List<Texture> textureList = new ArrayList<Texture>();
		for (String frame : textures) {
			textureList.add(new Texture(ImageUtils.getID(frame), width, height));
		}

		AnimationData animation = new AnimationData();
		animation.addAnimator("fire", new Animator(textureList, 3));
		this.addComponent(animation);

		ComponentFactory.addNameData(this, "tankgun");
		ComponentFactory.addLayerData(this, layer);
		this.addComponent(new PhysicsTransformWrapper());

		this.addComponent(new TextureRenderingLogic());

		this.addComponent(new PlayerGunAimerLogic(input));

		this.addComponent(new ShootingLogic(new BouncyMissile(new Circle(10), layer, new Texture(ImageUtils
				.getID("bomb.png"), 20, 20), 3, 10)));

		TreeLogic tree = new TreeLogic();

		ParallelComposite concurrent = new ParallelComposite();

		SequencerComposite firing = new SequencerComposite();
		firing.addChild(new LeftMouseHeldCondition(input));
		firing.addChild(new ActionExecutorLeaf<ShootingLogic>(ShootingLogic.class));
		concurrent.addChild(firing);
		concurrent.addChild(new ActionExecutorLeaf<PlayerGunAimerLogic>(PlayerGunAimerLogic.class));
		tree.setRoot(concurrent);

		this.addComponent(tree);
	}

	/**
	 * Sets the Tank this component belongs to.
	 * 
	 * @param tank
	 */
	@Override
	public void setTank(Tank tank) {
		m_tank = tank;
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

	public static class GunBuilder {
		private double width;
		private double height;
		private String[] textures;
		private double layer;
		private InputManager input;
		private TankBody body;
		private PhysicsActivity activity;

		public TankGun build() {
			return new TankGun(width, height, textures, layer, input, body, activity);
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
		public void setLayer(double layer) {
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
	}
}
