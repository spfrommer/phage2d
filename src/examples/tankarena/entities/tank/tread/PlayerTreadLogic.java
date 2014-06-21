package examples.tankarena.entities.tank.tread;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.component.Component;
import engine.core.framework.component.LogicComponent;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.leaf.action.executor.ActionExecutable;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.rendering.base.Animator;
import engine.core.implementation.rendering.data.AnimationData;
import engine.core.implementation.rendering.data.TextureData;
import engine.inputs.InputManager;

/**
 * Logic that lets a player control a tank's body.
 * 
 * @eng.dependencies PhysicsData, AnimationData
 */
public class PlayerTreadLogic extends LogicComponent implements ActionExecutable {
	private PhysicsData m_physics;
	private AnimationData m_animation;
	private TextureData m_texture;
	private InputManager m_inputManager;
	private int m_direction;

	public PlayerTreadLogic(InputManager inputManager, int direction) {
		super(new Aspect(TypeManager.getType(PhysicsData.class), TypeManager.getType(AnimationData.class),
				TypeManager.getType(TextureData.class)));
		m_inputManager = inputManager;
		m_direction = direction;
	}

	@Override
	public ExecutionState update(int ticks) {
		float forewards = m_inputManager.getValue("Forwards");
		float backwards = m_inputManager.getValue("Backwards");

		float left = m_inputManager.getValue("Left");
		float right = m_inputManager.getValue("Right");

		// + is forewards - is backwards
		float acceleration = forewards - backwards;
		// + is left - is right
		float turn = left - right;

		Vector direction = Vector.normalVector(-m_physics.getRotation());

		if (acceleration != 0f && turn == 0f) {
			m_physics.applyForce(direction.scalarMultiply(acceleration * ticks * 200000));

			Animator treads = m_animation.getAnimator((acceleration > 0) ? "treadforward" : "treadbackward");
			if (treads.isFinished() || treads.isReset())
				treads.animate(m_texture.texture);
		} else if (acceleration != 0f && turn != 0f) {
			m_physics.applyForce(direction.scalarMultiply(acceleration * ticks * 200000).add(
					direction.scalarMultiply(turn * m_direction * ticks * 1000000)));

			Animator treads = m_animation.getAnimator((acceleration > 0) ? "treadforward" : "treadbackward");
			if (treads.isFinished() || treads.isReset())
				treads.animate(m_texture.texture);
		} else if (acceleration == 0f && turn != 0f) {
			m_physics.applyForce(direction.scalarMultiply(turn * m_direction * ticks * 1000000));

			Animator treads = m_animation.getAnimator((turn * m_direction > 0) ? "treadforward" : "treadbackward");
			if (treads.isFinished() || treads.isReset())
				treads.animate(m_texture.texture);
		}
		return ExecutionState.RUNNING;
	}

	@Override
	public void loadDependencies() {
		m_physics = (PhysicsData) this.loadDependency(TypeManager.getType(PhysicsData.class));
		m_animation = (AnimationData) this.loadDependency(TypeManager.getType(AnimationData.class));
		m_texture = (TextureData) this.loadDependency(TypeManager.getType(TextureData.class));
	}

	@Override
	public Component copy() {
		return new PlayerTreadLogic(m_inputManager, m_direction);
	}

}
