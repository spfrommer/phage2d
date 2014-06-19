package examples.tankarena.entities.missile;

import org.dyn4j.geometry.Convex;

import utils.image.Texture;
import utils.physics.Vector;
import engine.core.factory.ComponentFactory;
import engine.core.framework.Entity;
import engine.core.implementation.network.logic.ServerLogic;
import engine.core.implementation.physics.wrappers.PhysicsTransformWrapper;

public class Missile extends Entity {
	public Missile(Convex collisionShape, double layer, Texture texture, double damage) {
		super();
		ComponentFactory.addPhysicsData(this, new Vector(0, 0), 0, collisionShape);

		ComponentFactory.addTextureData(this, texture);
		ComponentFactory.addNetworkData(this);
		ComponentFactory.addNameData(this, "tanktread");
		ComponentFactory.addLayerData(this, layer);
		ComponentFactory.addDamageData(this, damage);

		this.addComponent(new PhysicsTransformWrapper());
		this.addComponent(ComponentFactory.createBasicEncoder(this));
		this.addComponent(new ServerLogic());
	}
}
