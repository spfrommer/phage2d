package engine.core.implementation.physics.logic.handler;

import utils.image.ImageUtils;
import utils.image.Texture;
import engine.core.factory.ComponentFactory;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.extras.data.DamageData;
import engine.core.implementation.extras.data.HealthData;
import engine.core.implementation.network.logic.ServerLogic;
import engine.core.implementation.physics.wrappers.PositionWrapper;

/**
 * Handles a collision like a bullet - damages the entity it hits, destroys itself, and adds an explosion.
 * 
 * @eng.dependencies DamageData, PositionWrapper
 */
public class BulletCollisionHandlerLogic extends CollisionHandlerLogic {
	private EntitySystem m_system;
	private DamageData m_damage;
	private PositionWrapper m_position;

	public BulletCollisionHandlerLogic(EntitySystem system) {
		super(new Aspect(TypeManager.getType(DamageData.class), TypeManager.getType(PositionWrapper.class)));
		m_system = system;
	}

	public BulletCollisionHandlerLogic(Entity parent, EntitySystem system) {
		super(parent, new Aspect(TypeManager.getType(DamageData.class), TypeManager.getType(PositionWrapper.class)));
		m_system = system;
	}

	private Entity makeExplosion() {
		final Entity explosion = new Entity();

		ComponentFactory.addShellData(explosion, m_position.getPosition(), 0);
		ComponentFactory.addTextureData(explosion, new Texture(ImageUtils.getID("explosion.png"), 60, 60));
		ComponentFactory.addNetworkData(explosion);
		ComponentFactory.addNameData(explosion, "explosion");
		ComponentFactory.addLayerData(explosion, 0);
		ComponentFactory.addShellWrappers(explosion);
		explosion.addComponent(ComponentFactory.createBasicEncoder(explosion));

		explosion.addComponent(new ServerLogic(explosion));

		/*Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				BulletCollisionHandlerLogic.this.getSystem().removeEntity(explosion);
			}
		});
		thread.start();*/
		return explosion;
	}

	@Override
	public boolean handleCollision(Entity entity) {
		m_system.removeEntity(this.getEntity());
		if (entity.hasComponent(TypeManager.getType(HealthData.class))) {
			HealthData health = (HealthData) entity.getComponent(TypeManager.getType(HealthData.class));
			health.health -= m_damage.damage;
		}
		m_system.addEntity(makeExplosion());
		return false;
	}

	@Override
	public void loadDependencies() {
		m_damage = (DamageData) this.loadDependency(TypeManager.getType(DamageData.class));
		m_position = (PositionWrapper) this.loadDependency(TypeManager.getType(PositionWrapper.class));
	}

	protected EntitySystem getSystem() {
		return m_system;
	}

	@Override
	public Component copy() {
		return new BulletCollisionHandlerLogic(m_system);
	}
}
