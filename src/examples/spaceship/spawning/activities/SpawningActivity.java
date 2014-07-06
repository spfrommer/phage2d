package examples.spaceship.spawning.activities;

import java.util.List;

import engine.core.framework.Aspect;
import engine.core.framework.AspectActivity;
import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import examples.spaceship.spawning.logic.SpawningLogic;

/**
 * Calls upon Entities to spawn other Entities if need be.
 * 
 * @eng.dependencies SpawningLogic
 */
public class SpawningActivity extends AspectActivity {
	private ComponentType m_spawningType;

	{
		m_spawningType = TypeManager.typeOf(SpawningLogic.class);
	}

	public SpawningActivity(EntitySystem system) {
		super(system, new Aspect(TypeManager.typeOf(SpawningLogic.class)));
	}

	@Override
	public void entityAdded(Entity entity) {

	}

	@Override
	public void entityRemoved(Entity entity) {

	}

	/**
	 * Calls all SpawningLogic Entities and adds their spawned Entities.
	 * 
	 * @param system
	 * @param ticks
	 */
	public void spawn(EntitySystem system, int ticks) {
		List<Entity> entities = super.getEntities();
		for (Entity entity : entities) {
			SpawningLogic spawning = (SpawningLogic) entity.getComponent(m_spawningType);
			List<Entity> spawned = spawning.spawn(ticks);

			for (Entity spawn : spawned)
				system.addEntity(spawn);
		}
	}
}
