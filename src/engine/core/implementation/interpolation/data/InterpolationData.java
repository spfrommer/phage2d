package engine.core.implementation.interpolation.data;

import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;

public class InterpolationData extends DataComponent {
	public double lastX;
	public double lastY;
	public double lastRotation;
	public double lastTimeStamp;

	public double xDelta;
	public double yDelta;
	public double rotationDelta;

	public InterpolationData() {
		super();
	}

	public InterpolationData(Entity parent) {
		super(parent);
	}

	@Override
	public Component copy() {
		InterpolationData interpolation = new InterpolationData();
		interpolation.lastX = this.lastX;
		interpolation.lastY = this.lastY;
		interpolation.lastRotation = this.lastRotation;
		interpolation.lastTimeStamp = this.lastTimeStamp;
		interpolation.xDelta = this.xDelta;
		interpolation.yDelta = this.yDelta;
		interpolation.rotationDelta = this.rotationDelta;
		return interpolation;
	}
}
