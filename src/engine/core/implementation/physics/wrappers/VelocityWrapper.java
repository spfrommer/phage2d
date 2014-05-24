package engine.core.implementation.physics.wrappers;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.WrapperComponent;

/**
 * Abstracts the implementation behind getting the velocity of an Entity.
 */
public abstract class VelocityWrapper extends WrapperComponent {
	public VelocityWrapper(Aspect dependencies) {
		super(dependencies);
	}

	public VelocityWrapper(Entity parent, Aspect dependencies) {
		super(parent, dependencies);
	}

	public abstract Vector getVelocity();
}
