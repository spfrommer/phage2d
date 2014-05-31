package engine.core.implementation.behavior.logic.controller;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.LogicComponent;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.leaf.action.executor.ActionExecutable;
import engine.core.implementation.physics.data.PhysicsData;
import engine.inputs.InputManager;

/**
 * A preset controller. Controls an Entity with simple WASD strafing.
 * 
 * @eng.dependencies PhysicsData, InputManager
 */
public class WASDControllerLogic extends LogicComponent implements ActionExecutable {
	private PhysicsData m_physics;
	private InputManager m_inputManager;

	public WASDControllerLogic(InputManager manager) {
		super(new Aspect(TypeManager.getType(PhysicsData.class)));
		m_inputManager = manager;
	}

	public WASDControllerLogic(Entity parent, InputManager manager) {
		super(parent, new Aspect(TypeManager.getType(PhysicsData.class)));
		m_inputManager = manager;
	}

	@Override
	public ExecutionState update(int ticks) {
		float forewards = m_inputManager.getValue("Forwards");
		float backwards = m_inputManager.getValue("Backwards");

		float left = m_inputManager.getValue("Left");
		float right = m_inputManager.getValue("Right");

		// System.out.println(forewards);

		// + is forewards - is backwards
		float acceleration = forewards - backwards;
		// + is left - is right
		float turn = left - right;

		Vector v = Vector.normalVector(-m_physics.getRotation());

		m_physics.applyForce(v.scalarMultiply(acceleration * ticks * 1000));

		m_physics.applyTorque(turn * ticks * 5000);
		return ExecutionState.RUNNING;
	}

	@Override
	public void loadDependencies() {
		m_physics = (PhysicsData) this.loadDependency(TypeManager.getType(PhysicsData.class));
	}

	@Override
	public Component copy() {
		return new WASDControllerLogic(m_inputManager);
	}
}
