package migrate.gui;

import java.awt.Rectangle;

import migrate.input.Key;
import migrate.input.Keyboard;
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
	
	private FocusPolicy m_focusPolicy = null;
	
	private boolean m_mousedOver = false;	
	
	public int getX() { return m_x; }
	public int getY() { return m_y; }
	public int getWidth() { return m_width; }
	public int getHeight() { return m_height; }
	public FocusPolicy getFocusPolicy() { return m_focusPolicy; }
	public Dimension getMaxSize() { return m_maxSize; }
	public Dimension getMinSize() { return m_minSize; }
	public Dimension getPreferredSize() {
		//If not defined, fall back to minsize
		if (m_preferredSize == null) return getMinSize();
		return m_preferredSize;
	}
	
	public boolean isMousedOver() { return m_mousedOver; }
	public boolean isFocused() { return FocusManager.getInstance().isFocused(this); }
	//Will return if this widget, or any of this widget's children are focused
	public boolean hasFocusedChild() { return isFocused(); }
	
	public void setX(int x) { m_x = x; }
	public void setY(int y) { m_y = y; }
	public void setMaxSize(Dimension maxSize) { m_maxSize = maxSize; }
	public void setMinSize(Dimension minSize) { m_minSize = minSize; }
	public void setPreferredSize(Dimension preferredSize) { m_preferredSize = preferredSize; }
	public void setFocusPolicy(FocusPolicy policy) { m_focusPolicy = policy; }
	
	public void setWidth(int width) {
		//m_width = Math.max(Math.min(width, m_minSize.getWidth()), m_maxSize.getWidth());
		m_width = width;
		Dimension minSize = getMinSize();
		Dimension maxSize = getMaxSize();
		if (minSize != null) m_width = Math.max(m_width, minSize.getWidth());
		if (maxSize != null) m_width = Math.min(m_width, maxSize.getWidth());
	}
	public void setHeight(int height) {
		//m_height = Math.max(Math.min(height, m_minSize.getHeight()), m_maxSize.getHeight());
		m_height = height;
		Dimension minSize = getMinSize();
		Dimension maxSize = getMaxSize();
		if (minSize != null) m_height = Math.max(m_height, minSize.getHeight());
		if (maxSize != null) m_height = Math.min(m_height, maxSize.getHeight());
	}
	public void setBounds(int x, int y, int width, int height) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}
	public void setBounds(Rectangle r) {
		setBounds((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
	}
	
	/** Tests if the widget contains the given point
	 * 
	 * @param x the x coordinate, local to the Widget's parent
	 * @param y the y coordinate, local to the Widget's parent
	 * @return whether or not the widget contains the given point
	 */
	public boolean contains(int x, int y) {
		return (x > m_x) && (y > m_y) && (x < m_x + m_width) && (y < m_y + m_height);
	}
	
	public void requestFocus() {
		FocusManager.getInstance().requestFocus(this);
	}	
	
	/**
	 * This method tells the widget to lay out its children(if it has any)
	 */
	public void validate() {}
	
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
	 * Key and Mouse Listener interfaces, as the mouse coordinates
	 * are all local to the widget
	 */
	public void keyPressed(Keyboard keyboard, Key key) {
		if (m_focusPolicy != null) m_focusPolicy.keyPressed(this, keyboard, key);
	}
	public void keyReleased(Keyboard keyboard, Key key) {
		if (m_focusPolicy != null) m_focusPolicy.keyReleased(this, keyboard, key);
	}
	public void mouseMoved(Mouse m, int localX, int localY, Vector2f delta) {
		if (m_focusPolicy != null) m_focusPolicy.mouseMoved(this, m, localX, localY, delta);
	}
	public void mouseWheelMoved(Mouse m, int localX, int localY, int dm) {
		if (m_focusPolicy != null) m_focusPolicy.mouseWheelMoved(this, m, localX, localY, dm);
	}
	public void mouseDelta(Mouse m, int localX, int localY, int deltaX, int deltaY) {
		if (m_focusPolicy != null) m_focusPolicy.mouseDelta(this, m, localX, localY, deltaX, deltaY);
	}
	public void mouseButtonPressed(Mouse m, int localX, int localY, MouseButton button) {
		if (m_focusPolicy != null) m_focusPolicy.mouseButtonPressed(this, m, localX, localY, button);
	}
	public void mouseButtonReleased(Mouse m, int localX, int localY, MouseButton button) {
		if (m_focusPolicy != null) m_focusPolicy.mouseButtonReleased(this, m, localX, localY, button);
	}
	public void mouseEntered(Mouse m, int localX, int localY) {
		if (m_focusPolicy != null) m_focusPolicy.mouseEntered(this, m, localX, localY);
		m_mousedOver = true;
	}
	public void mouseExited(Mouse m, int localX, int localY) {
		if (m_focusPolicy != null) m_focusPolicy.mouseExited(this, m, localX, localY);
		m_mousedOver = false;
	}
}
