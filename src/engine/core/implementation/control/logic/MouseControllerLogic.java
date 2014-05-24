package engine.core.implementation.control.logic;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsData;
import engine.inputs.InputManager;

/**
 * Controls an Entity with PhysicsData to accelerate towards the mouse pointer.
 * 
 * @eng.dependencies PhysicsData, InputManager
 */
public class MouseControllerLogic extends ControllerLogic {
	private PhysicsData m_physics;
	private InputManager m_inputManager;

	public MouseControllerLogic(InputManager manager) {
		super(new Aspect(TypeManager.getType(PhysicsData.class)));
		m_inputManager = manager;
	}

	public MouseControllerLogic(Aspect dependences, InputManager manager) {
		super(new Aspect(TypeManager.getType(PhysicsData.class)).addTypes(dependences));
		m_inputManager = manager;
	}

	public MouseControllerLogic(Entity parent, InputManager manager) {
		super(parent, new Aspect(TypeManager.getType(PhysicsData.class)));
		m_inputManager = manager;
	}

	public MouseControllerLogic(Entity parent, Aspect dependencies, InputManager manager) {
		super(parent, new Aspect(TypeManager.getType(PhysicsData.class)).addTypes(dependencies));
		m_inputManager = manager;
	}

	/**
	 * Called when the Entity is accelerated towards the mouse.
	 */
	public void onAccelerate() {

	}

	/**
	 * Called when the Entity stops its acceleration.
	 */
	public void onDeccelerate() {

	}

	private float lastAcceleration = 0;

	@Override
	public void update(int ticks) {
		float mousex = m_inputManager.getValue("MouseWorldX");
		float mousey = m_inputManager.getValue("MouseWorldY");
		float acceleration = m_inputManager.getValue("LeftMouse");
		float powerUp = 1 + m_inputManager.getValue("t");
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

		Vector v = Vector.dyn4jNormalVector(m_physics.getRotation());

		m_physics.applyForce(v.scalarMultiply(acceleration * ticks * powerUp * 20000000));

		if (acceleration - lastAcceleration > 0) {
			onAccelerate();
		} else if (acceleration - lastAcceleration < 0) {
			onDeccelerate();
		}
		lastAcceleration = acceleration;
	}

	@Override
	public void loadDependencies() {
		m_physics = (PhysicsData) this.loadDependency(TypeManager.getType(PhysicsData.class));
	}

	protected InputManager getInputManager() {
		return m_inputManager;
	}

	@Override
	public Component copy() {
		return new MouseControllerLogic(m_inputManager);
	}
}
