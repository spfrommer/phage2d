package examples.tankarena.entities;

import utils.physics.Vector;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.LogicComponent;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.leaf.action.executor.ActionExecutable;
import engine.core.implementation.physics.data.PhysicsData;
import engine.inputs.InputManager;

/**
 * Logic that lets a player control a tank's body.
 */
public class PlayerTreadLogic extends LogicComponent implements ActionExecutable {
	private PhysicsData m_physics;
	private InputManager m_inputManager;
	private int m_direction;

	public PlayerTreadLogic(InputManager inputManager, int direction) {
		super();
		m_inputManager = inputManager;
		m_direction = direction;
	}

	public PlayerTreadLogic(Entity entity, InputManager inputManager, int direction) {
		super(entity);
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
			m_physics.applyForce(direction.scalarMultiply(acceleration * ticks * 100000));
		} else if (acceleration != 0f && turn != 0f) {
			m_physics.applyForce(direction.scalarMultiply(acceleration * ticks * 100000).add(
					direction.scalarMultiply(turn * m_direction * ticks * 500000)));
		} else if (acceleration == 0f && turn != 0f) {
			m_physics.applyForce(direction.scalarMultiply(turn * m_direction * ticks * 500000));
		}
		// if we are turning, but not accelerating

		// m_physics.applyTorque(turn * ticks * 10000000);
		return ExecutionState.RUNNING;
	}

	@Override
	public void loadDependencies() {
		m_physics = (PhysicsData) this.loadDependency(TypeManager.getType(PhysicsData.class));
	}

	@Override
	public Component copy() {
		return new PlayerTreadLogic(m_inputManager, m_direction);
	}

}
