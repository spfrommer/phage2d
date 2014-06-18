package examples.platformer;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.component.Component;
import engine.core.framework.component.LogicComponent;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.leaf.action.executor.ActionExecutable;
import engine.core.implementation.physics.data.PhysicsData;

/**
 * Will make the ball jump - return SUCCESS if the ball completed the jump and RUNNING if it is jumping.
 */
public class JumpControllerLogic extends LogicComponent implements ActionExecutable {
	private PhysicsData m_physics;
	// Our velocity one tick ago
	private int m_atRestTicks;

	public JumpControllerLogic() {
		super(new Aspect(TypeManager.getType(PhysicsData.class)));
	}

	@Override
	public ExecutionState update(int ticks) {
		if (Math.abs(m_physics.getVelocity().getY()) < 50) {
			m_atRestTicks += ticks;
			if (m_atRestTicks >= 30) {
				m_atRestTicks = 0;
				m_physics.applyForce(new Vector(0, 100000000));
				return ExecutionState.SUCCESS;
			}
		}

		return ExecutionState.RUNNING;
	}

	@Override
	public void loadDependencies() {
		m_physics = (PhysicsData) this.loadDependency(TypeManager.getType(PhysicsData.class));
	}

	@Override
	public Component copy() {
		return new JumpControllerLogic();
	}
}
