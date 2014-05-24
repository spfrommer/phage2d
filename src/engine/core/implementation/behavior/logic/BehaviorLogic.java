package engine.core.implementation.behavior.logic;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.SystemAspectManager;
import engine.core.framework.component.LogicComponent;

public abstract class BehaviorLogic extends LogicComponent {

	public BehaviorLogic(Entity entity, Aspect dependencies) {
		super(entity, dependencies);
	}

	public abstract void execute(long timeNanos, SystemAspectManager manager);
}
