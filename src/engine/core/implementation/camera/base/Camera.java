package engine.core.implementation.camera.base;

import java.awt.geom.AffineTransform;
import java.util.HashSet;

/**
 * A dumb camera that is controlled by a CameraProcess
 * 
 */
public class Camera {
	private HashSet<CameraListener> m_listeners;

	private double m_x;
	private double m_y;
	private double m_zoom;
	private double m_initialDisplayWidth;
	private double m_initialDisplayHeight;
	private double m_currentDisplayWidth;
	private double m_currentDisplayHeight;

	private double m_minZoom; // how far we can go out
	private double m_maxZoom; // how far we can go in

	{
		m_zoom = 0.3;
		m_minZoom = 0.08;
		m_maxZoom = 5;
		m_listeners = new HashSet<CameraListener>();
	}

	public void setViewPort(ViewPort port) {
		port.addResizeListener(new ResizeListener() {
			@Override
			public void resized(int width, int height) {
				m_currentDisplayWidth = width;
				m_currentDisplayHeight = height;

				if (m_initialDisplayWidth == 0)
					m_initialDisplayWidth = m_currentDisplayWidth;
				if (m_initialDisplayHeight == 0)
					m_initialDisplayHeight = m_currentDisplayHeight;
			}
		});
	}

	public void setMaxZoom(double maxZoom) {
		m_maxZoom = maxZoom;
	}

	public void setMinZoom(double minZoom) {
		m_minZoom = minZoom;
	}

	public void setX(double x) {
		m_x = x;
		notifyTransformChange();
	}

	public double getX() {
		return m_x;
	}

	public void setY(double y) {
		m_y = y;
		notifyTransformChange();
	}

	public double getY() {
		return m_y;
	}

	public void setZoom(double zoom) {
		m_zoom = Math.min(Math.max(m_minZoom, zoom), m_maxZoom);
		notifyTransformChange();
	}

	public double getZoom() {
		return m_zoom;
	}

	public void incrementX(double x) {
		m_x += x;
		notifyTransformChange();
	}

	public void incrementY(double y) {
		m_y += y;
		notifyTransformChange();
	}

	public void incrementZoom(double zoom) {
		m_zoom += zoom;
		m_zoom = Math.min(Math.max(m_minZoom, m_zoom), m_maxZoom);
		notifyTransformChange();
	}

	public AffineTransform getTransform() {
		AffineTransform at = new AffineTransform();
		at.translate(m_currentDisplayWidth / 2, m_currentDisplayHeight / 2);
		at.scale(m_zoom, m_zoom);
		at.translate(-m_currentDisplayWidth / 2, -m_currentDisplayHeight / 2);

		at.translate(m_currentDisplayWidth / 2, m_currentDisplayHeight / 2);

		at.translate(-m_x, m_y /*
								 * this should be -m_y, but because I'm flipping
								 * the axis after applying camera (for zoom
								 * compatability) I'm making it m_y
								 */);

		return at;
	}

	public void addListener(CameraListener cl) {
		m_listeners.add(cl);
	}

	public void removeListener(CameraListener cl) {
		m_listeners.remove(cl);
	}

	private void notifyTransformChange() {
		for (CameraListener cl : m_listeners)
			cl.transformChanged(this);
	}

	public interface CameraListener {
		public void transformChanged(Camera c);
	}
}
