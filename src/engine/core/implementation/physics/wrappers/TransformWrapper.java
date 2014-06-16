package engine.core.implementation.physics.wrappers;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.WrapperComponent;

/**
 * A wrapper for transformations that could be needed by rendering or other Components.
 */
public abstract class TransformWrapper extends WrapperComponent {
	public TransformWrapper(Aspect dependencies) {
		super(dependencies);
	}

	public TransformWrapper(Entity parent, Aspect dependencies) {
		super(parent, dependencies);
	}

	/**
	 * Returns the position of the transform.
	 * 
	 * @return
	 */
	public abstract Vector getPosition();

	/**
	 * Returns the center around which the rotation should be applied.
	 * 
	 * @return
	 */
	public abstract Vector getCenter();

	/**
	 * Returns the rotation of the transform.
	 * 
	 * @return
	 */
	public abstract double getRotation();
}
