package examples.tankarena.entities.tank;

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
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.composite.ParallelComposite;
import engine.core.implementation.behavior.base.composite.SequencerComposite;
import engine.core.implementation.behavior.base.leaf.action.executor.ActionExecutorLeaf;
import engine.core.implementation.behavior.logic.TreeLogic;
import engine.core.implementation.network.logic.ServerLogic;
import engine.core.implementation.physics.activities.PhysicsActivity;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.logic.filter.NoneCollisionFilterLogic;
import engine.core.implementation.physics.wrappers.PhysicsTransformWrapper;
import engine.core.implementation.rendering.base.Animator;
import engine.core.implementation.rendering.data.AnimationData;
import engine.core.implementation.rendering.logic.TextureRenderingLogic;
import engine.inputs.InputManager;
import examples.tankarena.entities.missile.Missile;

public class TankGun extends Entity {
	public TankGun(double width, double height, String[] textures, int layer, InputManager input, TankBody body,
			PhysicsActivity physicsActivity, EntitySystem system) {
		super();

		PhysicsData tankPhysics = (PhysicsData) body.getComponent(TypeManager.getType(PhysicsData.class));

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

		ComponentFactory.addNetworkData(this);
		ComponentFactory.addNameData(this, "tankgun");
		ComponentFactory.addLayerData(this, layer);
		this.addComponent(new PhysicsTransformWrapper());

		this.addComponent(ComponentFactory.createBasicEncoder(this));

		this.addComponent(new ServerLogic());

		// for single player mode
		this.addComponent(new TextureRenderingLogic());

		this.addComponent(new PlayerGunAimerLogic(input));
		Missile bouncy = new Missile(new Circle(10), layer, new Texture(ImageUtils.getID("portal1.png"), 20, 20));
		PhysicsData missilePhysics = (PhysicsData) bouncy.getComponent(TypeManager.getType(PhysicsData.class));
		missilePhysics.setRestitution(1);

		this.addComponent(new ShootingLogic(system, bouncy));

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
}
