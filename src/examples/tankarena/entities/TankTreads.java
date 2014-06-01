package examples.tankarena.entities;

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
import engine.inputs.InputManager;

public class TankTreads extends Entity {
	public TankTreads(Vector position, double width, double height, String texture, int layer, InputManager input,
			TankBody body, PhysicsActivity physicsActivity, int direction) {
		super();

		PhysicsData tankPhysics = (PhysicsData) body.getComponent(TypeManager.getType(PhysicsData.class));

		PhysicsData physics = ComponentFactory.addPhysicsData(this, position, 0, new Rectangle(width, height));
		physics.setRotationalFriction(0);
		physics.setMovementFriction(0);
		physics.setMass(50);

		Joint weld = PhysicsData.JointFactory.createJoint(tankPhysics, physics, position, JointType.WELD);
		physicsActivity.addJoint(weld);

		ComponentFactory.addTextureData(this, new Texture(ImageUtils.getID(texture), width, height));

		ComponentFactory.addNetworkData(this);
		ComponentFactory.addNameData(this, "tanktreads");
		ComponentFactory.addLayerData(this, layer);
		ComponentFactory.addPhysicsWrappers(this);

		this.addComponent(ComponentFactory.createBasicEncoder(this));

		this.addComponent(new ServerLogic(this));

		this.addComponent(new PlayerTreadLogic(this, input, direction));

		TreeLogic tree = new TreeLogic(this);
		tree.setRoot(new ActionExecutorLeaf<PlayerTreadLogic>(PlayerTreadLogic.class));
		this.addComponent(tree);
	}
}
