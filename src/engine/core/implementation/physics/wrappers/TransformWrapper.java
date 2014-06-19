package engine.core.implementation.physics.wrappers;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.component.WrapperComponent;

/**
 * A wrapper for transformations that could be needed by rendering or other Components.
 */
public abstract class TransformWrapper extends WrapperComponent {
	public TransformWrapper(Aspect dependencies) {
		super(dependencies);
	}

	/**
	 * Returns the position of the transform.
	 * 
	 * @return
	 */
	public abstract Vector getPosition();

	/**
	 * Sets the position of the transform.
	 * 
	 * @param vector
	 */
	public abstract void setPosition(Vector position);

	/**
	 * Returns the center around which the rotation should be applied.
	 * 
	 * @return
	 */
	public abstract Vector getCenter();

	/**
	 * Sets the center around which the rotation should be applied.
	 * 
	 * @param vector
	 */
	public abstract void setCenter(Vector center);

	/**
	 * Returns the rotation of the transform.
	 * 
	 * @return
	 */
	public abstract double getRotation();

	/**
	 * Sets the rotation of the transform
	 * 
	 * @param rotatio
	 */
	public abstract void setRotation(double rotation);
}
