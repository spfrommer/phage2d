package engine.core.implementation.camera.activities;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.camera.base.Camera;
import engine.core.implementation.camera.data.CameraFocusData;
import engine.core.implementation.physics.wrappers.TransformWrapper;

/**
 * An Activity that will control a Camera to chase an Entity.
 * 
 * @eng.dependencies CameraFocusData, TransformWrapper
 */
public class ChaseCameraActivity extends CameraActivity {
	private ComponentType m_focusType;
	private ComponentType m_transformType;

	/**
	 * Contains the focusID of the entity it should follow
	 */
	private int m_focusID;
	private Entity m_focusEntity;

	private double m_lastX;
	private double m_lastY;
	private static final double UPDATE_THRESHOLD = 1;

	{
		m_focusType = TypeManager.typeOf(CameraFocusData.class);
		m_transformType = TypeManager.typeOf(TransformWrapper.class);
	}

	/**
	 * Constructs a new ChaseCameraActivity, with the focusID equaling the CameraFocusComponent value in the Entity it
	 * should follow.
	 * 
	 * @param system
	 * @param focusID
	 */
	public ChaseCameraActivity(EntitySystem system, int focusID) {
		super(system, new Aspect(TypeManager.typeOf(CameraFocusData.class),
				TypeManager.typeOf(TransformWrapper.class)));
		m_focusID = focusID;
	}

	@Override
	public void control(Camera camera, int ticks) {
		if (m_focusEntity != null) {
			TransformWrapper position = (TransformWrapper) m_focusEntity.getComponent(m_transformType);
			Vector focus = position.getPosition();
			if (Math.abs(m_lastX - focus.getX()) > UPDATE_THRESHOLD) {
				camera.setX(focus.getX());
				m_lastX = focus.getX();
			}
			if (Math.abs(m_lastY - focus.getY()) > UPDATE_THRESHOLD) {
				camera.setY(focus.getY());
				m_lastY = focus.getY();
			}
		}
	}

	@Override
	public void entityAdded(Entity entity) {
		CameraFocusData focus = (CameraFocusData) entity.getComponent(m_focusType);
		if (focus.cameraID == m_focusID)
			m_focusEntity = entity;
	}

	@Override
	public void entityRemoved(Entity entity) {

	}
}
