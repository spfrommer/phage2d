package engine.core.implementation.extras.logic;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.LogicComponent;

public abstract class DeathLogic extends LogicComponent {
	public DeathLogic(Aspect dependencies) {
		super(dependencies);
	}

	public DeathLogic(Entity parent, Aspect dependencies) {
		super(parent, dependencies);
	}

	public abstract void die();
}
