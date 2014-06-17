package examples.spaceship;

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
import engine.core.implementation.physics.logic.handler.CollisionHandlerLogic;
import engine.core.implementation.physics.wrappers.ShellTransformWrapper;
import engine.core.implementation.physics.wrappers.TransformWrapper;

/**
 * Handles a collision like a bullet - damages the entity it hits, destroys itself, and adds an explosion.
 * 
 * @eng.dependencies DamageData, TransformWrapper
 */
public class BulletCollisionHandlerLogic extends CollisionHandlerLogic {
	private EntitySystem m_system;
	private DamageData m_damage;
	private TransformWrapper m_transform;

	public BulletCollisionHandlerLogic(EntitySystem system) {
		super(new Aspect(TypeManager.getType(DamageData.class), TypeManager.getType(TransformWrapper.class)));
		m_system = system;
	}

	public BulletCollisionHandlerLogic(Entity parent, EntitySystem system) {
		super(parent, new Aspect(TypeManager.getType(DamageData.class), TypeManager.getType(TransformWrapper.class)));
		m_system = system;
	}

	private Entity makeExplosion() {
		final Entity explosion = new Entity();

		ComponentFactory.addShellData(explosion, m_transform.getPosition(), 0);
		ComponentFactory.addTextureData(explosion, new Texture(ImageUtils.getID("explosion.png"), 60, 60));
		ComponentFactory.addNetworkData(explosion);
		ComponentFactory.addNameData(explosion, "explosion");
		ComponentFactory.addLayerData(explosion, 0);

		explosion.addComponent(new ShellTransformWrapper(explosion));
		explosion.addComponent(ComponentFactory.createBasicEncoder(explosion));
		explosion.addComponent(new ServerLogic(explosion));
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
		m_transform = (TransformWrapper) this.loadDependency(TypeManager.getType(TransformWrapper.class));
	}

	protected EntitySystem getSystem() {
		return m_system;
	}

	@Override
	public Component copy() {
		return new BulletCollisionHandlerLogic(m_system);
	}
}
