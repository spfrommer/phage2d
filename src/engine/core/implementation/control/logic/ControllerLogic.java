package engine.core.implementation.control.logic;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.LogicComponent;

public abstract class ControllerLogic extends LogicComponent {

	public ControllerLogic() {
		super();
	}

	public ControllerLogic(Entity parent) {
		super(parent);
	}

	public ControllerLogic(Aspect dependencies) {
		super(dependencies);
	}

	public ControllerLogic(Entity parent, Aspect dependencies) {
		super(parent, dependencies);
	}

	/**
	 * Updates this ControllerLogic.
	 * 
	 * @param ticks
	 *            the number of ticks elapsed
	 */
	public abstract void update(int ticks);
}
