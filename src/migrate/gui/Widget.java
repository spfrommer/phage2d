package migrate.gui;

import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;
import migrate.vector.Vector2f;
import engine.graphics.Renderable;
import engine.graphics.Renderer;

public abstract class Widget implements Renderable {
	private int m_x = 0;
	private int m_y = 0;
	private int m_width = 0;
	private int m_height = 0;
	
	private Dimension m_maxSize = null;
	private Dimension m_minSize = null;
	private Dimension m_preferredSize = null;
	
	private boolean m_mousedOver = false;
	
	public int getX() { return m_x; }
	public int getY() { return m_y; }
	public int getWidth() { return m_width; }
	public int getHeight() { return m_height; }
	public Dimension getMaxSize() { return m_maxSize; }
	public Dimension getMinSize() { return m_minSize; }
	public Dimension getPreferrredSize() { return m_preferredSize; }
	
	public boolean isMousedOver() { return m_mousedOver; }
	
	public void setX(int x) { m_x = x; }
	public void setY(int y) { m_y = y; }
	public void setWidth(int width) { m_width = width; }
	public void setHeight(int height) { m_height = height; }
	public void setMaxSize(Dimension maxSize) { m_maxSize = maxSize; }
	public void setMinSize(Dimension minSize) { m_minSize = minSize; }
	public void setPreferredSize(Dimension preferredSize) { m_preferredSize = preferredSize; }
	
	public void setMousedOver(boolean moused) { m_mousedOver = moused; }
	
	/** Tests if the widget contains the given point
	 * 
	 * @param x the x coordinate, local to the Widget's parent
	 * @param y the y coordinate, local to the Widget's parent
	 * @return whether or not the widget contains the given point
	 */
	public boolean contains(int x, int y) {
		return (x > m_x) && (y > m_y) && (x < m_x + m_width) && (y < m_y + m_height);
	}
	
	public void render(Renderer r) {
		r.pushTransform();
		r.translate(m_x, m_y);
		renderWidget(r);
		r.popTransform();
	}
	public abstract void renderWidget(Renderer r);
	
	
	//-----Input methods-------
	/*
	 * The following methods are not the same as the
	 * Key and Mouse Listener interfaces, as the coordinates
	 * are all local to the widget
	 */
	
	public void mouseMoved(Mouse m, int localX, int localY, Vector2f delta) {}
	public void mouseWheelMoved(Mouse m, int localX, int localY, int dm) {}
	public void mouseDelta(Mouse m, int localX, int localY, int deltaX, int deltaY) {}
	public void mouseButtonPressed(Mouse m, int localX, int localY, MouseButton button) {}
	public void mouseButtonReleased(Mouse m, int localX, int localY, MouseButton button) {}
	public void mouseEntered(Mouse m, int localX, int localY) {}
	public void mouseExited(Mouse m, int localX, int localY) {}
}
