package examples.tankarena.entities.effects;

import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;
import engine.core.implementation.behavior.base.leaf.action.ActionLeaf;
import engine.core.implementation.rendering.base.Animator;
import engine.core.implementation.rendering.data.AnimationData;
import engine.core.implementation.rendering.data.TextureData;

public class AnimationAction extends ActionLeaf {
	private TextureData m_texture;
	private AnimationData m_animation;

	public AnimationAction() {
	}

	@Override
	public boolean load(Entity entity) {
		try {
			m_texture = (TextureData) entity.getComponent(TypeManager.getType(TextureData.class));
			m_animation = (AnimationData) entity.getComponent(TypeManager.getType(AnimationData.class));
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@Override
	public Node copy() {
		return new AnimationAction();
	}

	@Override
	public ExecutionState update(int ticks) {
		Animator effectAnimator = m_animation.getAnimator("effect");
		if (effectAnimator.isFinished()) {
			effectAnimator.animate(m_texture.texture);
			return ExecutionState.SUCCESS;
		}
		return ExecutionState.RUNNING;
	}
}
