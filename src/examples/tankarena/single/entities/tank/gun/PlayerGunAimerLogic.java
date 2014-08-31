package examples.tankarena.single.entities.tank.gun;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.component.Component;
import engine.core.framework.component.LogicComponent;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.leaf.action.executor.ActionExecutable;
import engine.core.implementation.physics.data.PhysicsData;
import engine.inputs.InputManager;
import examples.spaceship.MouseControllerLogic;

/**
 * Controls a gun.
 * 
 * @eng.dependencies PhysicsData, AnimationDAta
 */
public class PlayerGunAimerLogic extends LogicComponent implements ActionExecutable {
	private PhysicsData m_physics;
	private InputManager m_inputManager;

	public PlayerGunAimerLogic(InputManager manager) {
		super(new Aspect(TypeManager.typeOf(PhysicsData.class)));
		m_inputManager = manager;
	}

	public PlayerGunAimerLogic(Aspect dependences, InputManager manager) {
		super(new Aspect(TypeManager.typeOf(PhysicsData.class)).addTypes(dependences));
		m_inputManager = manager;
	}

	@Override
	public ExecutionState update(int ticks) {
		float mousex = m_inputManager.getValue("MouseWorldX");
		float mousey = m_inputManager.getValue("MouseWorldY");
		double desired = -(new Vector(mousex, mousey).subtract(m_physics.getPosition()).angle()) + 180;
		double currentRotation = m_physics.getRotation() + 180;
		if (Math.abs(desired - currentRotation) < 180) {
			m_physics.setRotationalVelocity((desired - currentRotation) * 0.1);
		} else {
			if (desired - currentRotation > 180) {
				m_physics.setRotationalVelocity(-(360 + currentRotation - desired) * 0.1);
			} else if (desired - currentRotation < -180) {
				m_physics.setRotationalVelocity((360 - currentRotation + desired) * 0.1);
			}
		}

		return ExecutionState.RUNNING;
	}

	@Override
	public void loadDependencies() {
		m_physics = (PhysicsData) this.loadDependency(TypeManager.typeOf(PhysicsData.class));
	}

	protected InputManager getInputManager() {
		return m_inputManager;
	}

	@Override
	public Component copy() {
		return new MouseControllerLogic(m_inputManager);
	}
}
