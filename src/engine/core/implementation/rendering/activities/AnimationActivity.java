package engine.core.implementation.rendering.activities;

import java.util.List;

import engine.core.framework.Aspect;
import engine.core.framework.AspectActivity;
import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.rendering.base.Animator;
import engine.core.implementation.rendering.data.AnimationData;

/**
 * Updates animations in Entities that have them.
 * 
 * @eng.dependencies AnimationData
 */
public class AnimationActivity extends AspectActivity {
	private ComponentType m_animationType;

	{
		m_animationType = TypeManager.typeOf(AnimationData.class);
	}

	public AnimationActivity(EntitySystem system) {
		super(system, new Aspect(TypeManager.typeOf(AnimationData.class)));
	}

	public void update(int ticks) {
		List<Entity> entities = super.getEntities();

		for (Entity entity : entities) {
			AnimationData animation = (AnimationData) entity.getComponent(m_animationType);
			for (Animator animate : animation.getAnimators()) {
				animate.updateAnimation(ticks);
			}
		}
	}

	@Override
	public void entityAdded(Entity entity) {

	}

	@Override
	public void entityRemoved(Entity entity) {

	}
}
