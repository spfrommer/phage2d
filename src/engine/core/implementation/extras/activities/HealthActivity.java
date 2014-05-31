package engine.core.implementation.extras.activities;

import java.util.List;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.AspectActivity;
import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.extras.data.HealthData;
import engine.core.implementation.physics.data.PhysicsData;

/**
 * Detects if an Entity is dead, then respawns it with 200 health.
 * 
 * @eng.dependencies HealthData, PhysicsData
 */
public class HealthActivity extends AspectActivity {
	private ComponentType m_healthType;
	private ComponentType m_physicsType;

	{
		m_healthType = TypeManager.getType(HealthData.class);
		m_physicsType = TypeManager.getType(PhysicsData.class);
	}

	public HealthActivity(EntitySystem system) {
		super(system, new Aspect(TypeManager.getType(HealthData.class), TypeManager.getType(PhysicsData.class)));
	}

	@Override
	public void entityAdded(Entity entity) {

	}

	@Override
	public void entityRemoved(Entity entity) {

	}

	public void update(int ticks) {
		List<Entity> entities = this.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			HealthData health = (HealthData) entity.getComponent(m_healthType);

			if (health.health <= 0) {
				System.out.println("Entity: " + entity + " is dead.");
				health.health = 200;
				PhysicsData physics = (PhysicsData) entity.getComponent(m_physicsType);
				physics.setPosition(new Vector(0, 0));
			}
		}
	}
}
