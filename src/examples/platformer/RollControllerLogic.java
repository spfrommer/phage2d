package examples.platformer;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.LogicComponent;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.leaf.action.executor.ActionExecutable;
import engine.core.implementation.physics.data.PhysicsData;

/**
 * Applies a torque to an object - would best be used in conjunction with a TimeDecorator since it always returns
 * Running.
 */
public class RollControllerLogic extends LogicComponent implements ActionExecutable {
	private PhysicsData m_physics;
	private double m_torque;

	public RollControllerLogic(double torque) {
		super(new Aspect(TypeManager.getType(PhysicsData.class)));
		m_torque = torque;
	}

	public RollControllerLogic(Entity parent, double torque) {
		super(parent, new Aspect(TypeManager.getType(PhysicsData.class)));
		m_torque = torque;
	}

	@Override
	public ExecutionState update(int ticks) {
		m_physics.applyTorque(m_torque);
		return ExecutionState.RUNNING;
	}

	@Override
	public void loadDependencies() {
		m_physics = (PhysicsData) this.loadDependency(TypeManager.getType(PhysicsData.class));
	}

	@Override
	public Component copy() {
		return new RollControllerLogic(m_torque);
	}
}
