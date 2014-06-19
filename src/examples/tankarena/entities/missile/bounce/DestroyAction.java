package examples.tankarena.entities.missile.bounce;

import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;
import engine.core.implementation.behavior.base.leaf.action.ActionLeaf;
import engine.core.implementation.extras.data.DamageData;
import engine.core.implementation.extras.data.HealthData;

public class DestroyAction extends ActionLeaf {
	private DamageData m_damage;
	private BounceData m_bounce;

	private ComponentType m_healthType;

	private EntitySystem m_system;

	private Entity m_entity;

	{
		m_healthType = TypeManager.getType(HealthData.class);
	}

	public DestroyAction(EntitySystem system) {
		super();
		m_system = system;
	}

	@Override
	public boolean load(Entity entity) {
		m_entity = entity;
		try {
			m_damage = (DamageData) entity.getComponent(TypeManager.getType(DamageData.class));
			m_bounce = (BounceData) entity.getComponent(TypeManager.getType(BounceData.class));
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@Override
	public Node copy() {
		return new DestroyAction(m_system);
	}

	@Override
	public ExecutionState update(int ticks) {
		Entity collided = m_bounce.bouncedAgainst;
		m_system.removeEntity(m_entity);
		if (!collided.hasComponent(m_healthType)) {
			System.out.println("Did no damage");
			return ExecutionState.FAILURE;
		} else {
			HealthData health = (HealthData) collided.getComponent(m_healthType);
			health.health -= m_damage.damage;
			System.out.println("Did damage: " + m_damage.damage);
			return ExecutionState.SUCCESS;
		}
	}
}
