package examples.tankarena.entities.missile.bounce;

import org.dyn4j.geometry.Convex;

import utils.image.Texture;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.composite.SequencerComposite;
import engine.core.implementation.behavior.logic.TreeLogic;
import engine.core.implementation.physics.data.PhysicsData;
import examples.tankarena.entities.missile.Missile;

public class BouncyMissile extends Missile {
	public BouncyMissile(Convex collisionShape, double layer, Texture texture, EntitySystem system, int maxBounces,
			double damage) {
		super(collisionShape, layer, texture, damage);

		PhysicsData missilePhysics = (PhysicsData) this.getComponent(TypeManager.getType(PhysicsData.class));
		missilePhysics.setRestitution(1);

		BounceData bounce = new BounceData();
		bounce.maxBounces = maxBounces;
		this.addComponent(bounce);

		this.addComponent(new BounceCollisionLogic());

		TreeLogic behavior = new TreeLogic();
		SequencerComposite sequencer = new SequencerComposite();
		// sequencer.addChild(new BounceCondition());
		sequencer.addChild(new DestroyAction(system));
		behavior.setRoot(sequencer);
		this.addComponent(behavior);
	}
}
