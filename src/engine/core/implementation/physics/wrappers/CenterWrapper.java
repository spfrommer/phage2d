package engine.core.implementation.physics.wrappers;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.WrapperComponent;

/**
 * Abstracts the implementation behind getting the center of an Entity.
 */
public abstract class CenterWrapper extends WrapperComponent {
	public CenterWrapper(Aspect dependencies) {
		super(dependencies);
	}

	public CenterWrapper(Entity parent, Aspect dependencies) {
		super(parent, dependencies);
	}

	public abstract Vector getCenter();
}
