package examples.tankarena.entities.effects;

import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;
import engine.core.implementation.behavior.base.leaf.action.ActionLeaf;
import engine.core.implementation.rendering.base.Animator;
import engine.core.implementation.rendering.data.AnimationData;

public class AnimationAction extends ActionLeaf {
	private AnimationData m_animation;
	private EntitySystem m_system;
	private Entity m_entity;

	public AnimationAction(EntitySystem system) {
		m_system = system;
	}

	@Override
	public boolean load(Entity entity) {
		m_entity = entity;
		try {
			m_animation = (AnimationData) entity.getComponent(TypeManager.getType(AnimationData.class));
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@Override
	public Node copy() {
		return new AnimationAction(m_system);
	}

	@Override
	public ExecutionState update(int ticks) {
		Animator effectAnimator = m_animation.getAnimator("effect");
		if (effectAnimator.isFinished()) {
			m_system.removeEntity(m_entity);
			return ExecutionState.SUCCESS;
		}
		return ExecutionState.RUNNING;
	}
}
