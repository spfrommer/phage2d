package engine.core.implementation.behavior.logic.controller;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.rendering.base.Animator;
import engine.core.implementation.rendering.data.AnimationData;
import engine.core.implementation.rendering.data.TextureData;
import engine.inputs.InputManager;

/**
 * A preset controller. Does the same as MouseControllerLogic, but plays an animation on the start and stop of the
 * Entity, as specified by the "start" and "stop" tags.
 * 
 * @eng.dependencies TextureData, AnimationData
 */
public class AnimatedControllerLogic extends MouseControllerLogic {
	private TextureData m_texture;
	private AnimationData m_animation;

	public AnimatedControllerLogic(InputManager manager) {
		super(new Aspect(TypeManager.getType(TextureData.class), TypeManager.getType(AnimationData.class)), manager);
	}

	public AnimatedControllerLogic(Entity parent, InputManager manager) {
		super(parent, new Aspect(TypeManager.getType(TextureData.class), TypeManager.getType(AnimationData.class)),
				manager);
	}

	@Override
	public void onAccelerate() {
		Animator animator = m_animation.getAnimator("start");
		animator.animate(m_texture.texture);
	}

	@Override
	public void onDeccelerate() {
		Animator animator = m_animation.getAnimator("stop");
		animator.animate(m_texture.texture);
	}

	@Override
	public void loadDependencies() {
		super.loadDependencies();
		m_texture = (TextureData) this.loadDependency(TypeManager.getType(TextureData.class));
		m_animation = (AnimationData) this.loadDependency(TypeManager.getType(AnimationData.class));
	}

	@Override
	public Component copy() {
		return new AnimatedControllerLogic(this.getInputManager());
	}
}
