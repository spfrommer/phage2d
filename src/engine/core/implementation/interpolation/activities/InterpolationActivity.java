package engine.core.implementation.interpolation.activities;

import java.util.List;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.AspectActivity;
import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.interpolation.base.TransmissionListener;
import engine.core.implementation.interpolation.base.TransmissionReceiver;
import engine.core.implementation.interpolation.data.InterpolationData;
import engine.core.implementation.physics.wrappers.TransformWrapper;

/**
 * Interpolates the transform using timestamps and transform data stored in InterpolationData. Uses a simple
 * "dead reckoning" approach.
 * 
 * @eng.dependencies TransformWrapper, InterpolationData
 */
public class InterpolationActivity extends AspectActivity {
	private ComponentType m_transformType;
	private ComponentType m_interpolationType;
	private double m_lastTime = System.currentTimeMillis();

	{
		m_transformType = TypeManager.getType(TransformWrapper.class);
		m_interpolationType = TypeManager.getType(InterpolationData.class);
	}

	public InterpolationActivity(EntitySystem system, TransmissionReceiver receiver) {
		super(system, new Aspect(TypeManager.getType(TransformWrapper.class),
				TypeManager.getType(InterpolationData.class)));

		receiver.addTransmissionListener(new UpdateListener());
	}

	public void update() {
		List<Entity> entities = this.getEntities();
		// concurrent modification exception?
		double time = System.currentTimeMillis();
		for (Entity entity : entities) {
			TransformWrapper transform = (TransformWrapper) entity.getComponent(m_transformType);
			InterpolationData interpolation = (InterpolationData) entity.getComponent(m_interpolationType);
			double timeDelta = time - m_lastTime;

			Vector position = transform.getPosition();
			Vector update = new Vector(interpolation.xDelta * timeDelta, interpolation.yDelta * timeDelta);
			// System.out.println(update);
			transform.setPosition(position.add(update));

			transform.setRotation(transform.getRotation() + interpolation.rotationDelta * timeDelta);
		}
		m_lastTime = time;
	}

	@Override
	public void entityAdded(Entity entity) {
		TransformWrapper transform = (TransformWrapper) entity.getComponent(m_transformType);
		InterpolationData interpolation = (InterpolationData) entity.getComponent(m_interpolationType);

		interpolation.lastRotation = transform.getRotation();
		interpolation.lastX = transform.getPosition().getX();
		interpolation.lastY = transform.getPosition().getY();
		interpolation.lastTimeStamp = System.currentTimeMillis();
	}

	@Override
	public void entityRemoved(Entity entity) {

	}

	private class UpdateListener implements TransmissionListener {
		@Override
		public void transmissionReceived(Entity receiver) {
			if (receiver.getAspect().encapsulates(new Aspect(m_transformType, m_interpolationType))) {
				double currentTime = System.currentTimeMillis();

				TransformWrapper transform = (TransformWrapper) receiver.getComponent(m_transformType);
				InterpolationData interpolation = (InterpolationData) receiver.getComponent(m_interpolationType);

				// calculate deltas from last transmission
				double timeDifference = currentTime - interpolation.lastTimeStamp;
				double xDifference = transform.getPosition().getX() - interpolation.lastX;
				double yDifference = transform.getPosition().getY() - interpolation.lastY;
				double rotationDifference = transform.getRotation() - interpolation.lastRotation;
				// System.out.println("Prior rot dif: " + rotationDifference);
				if (Math.abs(rotationDifference) > 180) {
					double sign = rotationDifference / Math.abs(rotationDifference);
					rotationDifference = -sign * (360 - Math.abs(rotationDifference));
				}
				// System.out.println("Rotation difference: " + rotationDifference);

				interpolation.xDelta = xDifference / timeDifference;
				interpolation.yDelta = yDifference / timeDifference;
				interpolation.rotationDelta = rotationDifference / timeDifference;

				// update last transform
				interpolation.lastX = transform.getPosition().getX();
				interpolation.lastY = transform.getPosition().getY();
				interpolation.lastRotation = transform.getRotation();
				interpolation.lastTimeStamp = currentTime;
			}
		}
	}
}
