package engine.core.implementation.camera.base;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import engine.graphics.Renderer;

// a viewport for split screen, etc...
public class ViewPort {
	private List<ResizeListener> m_listeners = new ArrayList<ResizeListener>();

	private int m_x;
	private int m_y;
	private int m_width;
	private int m_height;
	private Camera m_camera;

	public ViewPort(Camera camera) {
		m_camera = camera;

		m_camera.setViewPort(this);
	}

	public AffineTransform getTransform() {
		AffineTransform trans = (flipAxis(m_camera.getTransform()));
		return trans;
	}

	private AffineTransform flipAxis(AffineTransform at) {
		at.scale(1, -1);
		return at;
	}

	public void resized(int width, int height) {
		m_width = width;
		m_height = height;
		for (ResizeListener listener : m_listeners)
			listener.resized(width, height);
	}

	public int getX() {
		return m_x;
	}

	public int getY() {
		return m_y;
	}

	public int getWidth() {
		return m_width;
	}

	public int getHeight() {
		return m_height;
	}

	public void setX(int x) {
		m_x = x;
	}

	public void setY(int y) {
		m_y = y;
	}

	/*public void setWidth(int w) {
		m_width = w;
	}

	public void setHeight(int h) {
		m_height = h;
	}*/

	public void setLocation(int x, int y) {
		setX(x);
		setY(y);
	}

	public Rectangle getViewShape() {
		return new Rectangle(0, 0, getWidth(), getHeight());
	}

	public void addResizeListener(ResizeListener listener) {
		m_listeners.add(listener);
		// Update the listener with the current width and height
		listener.resized(m_width, m_height);
	}

	public void lookThrough(Renderer r) {
		r.transform(getTransform());
	}

	public Camera getCamera() {
		return m_camera;
	}

	/*public void render(Renderer r) {
		r.pushTransform();
		// r.setClip(getViewArea());
		// r.translate(m_x, m_y);
		r.transform(getTransform());

		Shape view = null;
		try {
			view = m_camera.getTransform().createInverse().createTransformedShape(getViewShape());
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}

		// r.setClip(null);
		r.popTransform();
	}*/
}
