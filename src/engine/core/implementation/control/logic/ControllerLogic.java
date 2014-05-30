package engine.core.implementation.control.logic;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.LogicComponent;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.leaf.action.executor.ActionExecutable;

/**
 * Controls an Entity - can be inserted into a tree as an ActionExecutorLeaf.
 */
public abstract class ControllerLogic extends LogicComponent implements ActionExecutable {

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
	@Override
	public abstract ExecutionState update(int ticks);
}
