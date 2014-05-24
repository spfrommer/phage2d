package engine.core.implementation.extras.logic;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.rendering.base.Animator;
import engine.core.implementation.rendering.data.AnimationData;
import engine.core.implementation.rendering.data.TextureData;

public class AnimationDeathLogic extends DeathLogic {
	private TextureData m_texture;
	private AnimationData m_animation;

	public AnimationDeathLogic() {
		super(new Aspect(TypeManager.getType(TextureData.class), TypeManager.getType(AnimationData.class)));
	}

	public AnimationDeathLogic(Entity parent) {
		super(parent, new Aspect(new ComponentType[] { TypeManager.getType(TextureData.class),
				TypeManager.getType(AnimationData.class) }));
	}

	@Override
	public void die() {
		Animator animator = m_animation.getAnimator("death");
		animator.animate(m_texture.texture);
	}

	@Override
	public void loadDependencies() {
		m_texture = (TextureData) this.loadDependency(TypeManager.getType(TextureData.class));
		m_animation = (AnimationData) this.loadDependency(TypeManager.getType(AnimationData.class));
	}

	@Override
	public Component copy() {
		return new AnimationDeathLogic();
	}
}
