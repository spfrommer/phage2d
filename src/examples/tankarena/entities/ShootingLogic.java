package examples.tankarena.entities;

import engine.core.framework.component.Component;
import engine.core.framework.component.LogicComponent;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.leaf.action.executor.ActionExecutable;

public class ShootingLogic extends LogicComponent implements ActionExecutable {

	@Override
	public void loadDependencies() {

	}

	@Override
	public Component copy() {
		return null;
	}

	@Override
	public ExecutionState update(int ticks) {
		System.out.println("Shooting");
		return ExecutionState.SUCCESS;
	}

}
