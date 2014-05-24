package engine.core.implementation.physics.wrappers;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.WrapperComponent;

/**
 * Abstracts the implementation behind getting the rotation of an Entity.
 */
public abstract class RotationWrapper extends WrapperComponent {
	public RotationWrapper(Aspect dependencies) {
		super(dependencies);
	}

	public RotationWrapper(Entity parent, Aspect dependencies) {
		super(parent, dependencies);
	}

	public abstract double getRotation();
}
