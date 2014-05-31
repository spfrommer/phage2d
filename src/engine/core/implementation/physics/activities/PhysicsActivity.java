package engine.core.implementation.physics.activities;

import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactConstraint;

import utils.TwoWayHashMap;
import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.AspectActivity;
import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.logic.handler.CollisionHandlerLogic;

/**
 * Manages the physics bodies of Entities in a world.
 * 
 * @eng.dependencies PhysicsData
 */
public class PhysicsActivity extends AspectActivity {
	private World m_world;
	private TwoWayHashMap<Entity, Integer> m_idMapper;
	private int currentID;

	{
		m_world = new World();
		m_world.getSettings().setMaximumRotation(1000000000);
		m_world.getSettings().setMaximumTranslation(1000000000);
		m_idMapper = new TwoWayHashMap<Entity, Integer>();
	}

	public PhysicsActivity(EntitySystem system) {
		super(system, new Aspect(TypeManager.getType(PhysicsData.class)));

		listenForCollisions();
	}

	private void listenForCollisions() {
		m_world.addListener(new CollisionListener() {
			@Override
			public boolean collision(ContactConstraint arg0) {
				Entity entity1 = m_idMapper.getBackward((Integer) arg0.getBody1().getUserData());
				Entity entity2 = m_idMapper.getBackward((Integer) arg0.getBody2().getUserData());
				boolean continue1 = true;
				boolean continue2 = true;

				ComponentType collisionHandlerType = TypeManager.getType(CollisionHandlerLogic.class);

				if (entity1.getAspect().encapsulates(new Aspect(collisionHandlerType))) {
					continue1 = ((CollisionHandlerLogic) entity1.getComponent(collisionHandlerType))
							.handleCollision(entity2);
				}

				if (entity2.getAspect().encapsulates(new Aspect(collisionHandlerType))) {
					continue2 = ((CollisionHandlerLogic) entity2.getComponent(collisionHandlerType))
							.handleCollision(entity1);
				}

				return continue1 && continue2;
			}

			@Override
			public boolean collision(Body arg0, Body arg1) {
				return true;
			}

			@Override
			public boolean collision(Body arg0, BodyFixture arg1, Body arg2, BodyFixture arg3, Penetration arg4) {
				return true;
			}

			@Override
			public boolean collision(Body arg0, BodyFixture arg1, Body arg2, BodyFixture arg3, Manifold arg4) {
				return true;
			}
		});
	}

	/**
	 * Sets the gravity of the world. Default is 9.8 m/s in the negative y direction.
	 * 
	 * @param vector
	 */
	public void setGravity(Vector vector) {
		m_world.setGravity(vector.toPhysicsVector());
	}

	/**
	 * Gets the gravity of the world. Default is 9.8 m/s in the negative y direction.
	 * 
	 * @return
	 */
	public Vector getGravity() {
		return new Vector(m_world.getGravity());
	}

	/**
	 * Updates the world.
	 */
	public void update(int ticks) {
		m_world.update(ticks);
	}

	@Override
	public void entityAdded(Entity entity) {
		PhysicsData physics = (PhysicsData) entity.getComponent(TypeManager.getType(PhysicsData.class));
		physics.addToWorld(m_world);

		int id = physics.getID();
		if (id == -1) {
			id = currentID++;
			m_idMapper.put(entity, id);
		} else {
			id = currentID++;
			m_idMapper.put(entity, id);
		}

		physics.setID(id);
	}

	@Override
	public void entityRemoved(Entity entity) {
		PhysicsData physics = (PhysicsData) entity.getComponent(TypeManager.getType(PhysicsData.class));
		physics.removeFromWorld(m_world);
	}
}
