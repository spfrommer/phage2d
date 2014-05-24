package engine.core.implementation.physics.wrappers;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.WrapperComponent;

/**
 * Abstracts the implementation behind getting the position of an Entity.
 */
public abstract class PositionWrapper extends WrapperComponent {
	public PositionWrapper(Aspect dependencies) {
		super(dependencies);
	}

	public PositionWrapper(Entity parent, Aspect dependencies) {
		super(parent, dependencies);
	}

	public abstract Vector getPosition();
}
