package engine.inputs.mouse;

import java.awt.geom.NoninvertibleTransformException;

import utils.physics.Vector;
import engine.core.implementation.camera.base.Camera;
import engine.core.implementation.camera.base.Camera.CameraListener;
import engine.core.implementation.camera.base.ViewPort;
import engine.inputs.InputTrigger;

public class MouseWorldYTrigger extends InputTrigger implements MouseListener, CameraListener {
	private Camera m_cam;
	private Mouse m_mouse;
	private ViewPort m_port;
	
	public MouseWorldYTrigger() {}
	public MouseWorldYTrigger(Mouse mouse, ViewPort port) {
		m_mouse = mouse;
		m_cam = port.getCamera();
		m_port = port;
		mouse.addMouseListener(this);
		m_cam.addListener(this);
	}
	private Vector getWorldPos() {
		try {
			return m_mouse.getCoordinates().inverseTransform(m_port.getTransform());
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void mouseEvent(MouseEvent e) {
		if (e instanceof MouseMovedEvent) {
			Vector pos = getWorldPos();
			trigger((float) (pos.getY()));
		}
	}
	@Override
	public void transformChanged(Camera c) {
		trigger((float) (getWorldPos().getY()));
	}
}