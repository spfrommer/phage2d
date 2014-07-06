package migrate.gui.widgets;

import java.awt.Rectangle;

import migrate.gui.Panel;
import migrate.gui.Widget;
import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;
import migrate.vector.Vector2f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import engine.graphics.Renderer;

public abstract class Window extends Widget {
	private static final Logger logger = LoggerFactory.getLogger(Window.class);
	
	private boolean m_moving = false;
	
	private Panel m_contentPane = new Panel();
	
	public Window() {}
	
	public Panel getContentPane() { return m_contentPane; }
	
	public boolean isMoving() { return m_moving; }
	
	protected abstract Rectangle getContentPaneBounds();
	protected abstract Rectangle getWindowBarBounds();
	
	public void renderWidget(Renderer r) {
		renderFrame(r);
		
		//Update the content pane incase the bounds have changed
		Rectangle contentBounds = getContentPaneBounds();
		m_contentPane.setX((int) contentBounds.getX());
		m_contentPane.setY((int) contentBounds.getY());
		m_contentPane.setWidth((int) contentBounds.getWidth());
		m_contentPane.setHeight((int) contentBounds.getHeight());
		
		m_contentPane.render(r);
	}
	public abstract void renderFrame(Renderer r);

	//----Input Methods------
	/*
	 * A window will only redistribute a input call to its contentPane
	 */
	@Override
	public void mouseMoved(Mouse m, int localX, int localY, Vector2f delta) {
		if (m_contentPane.contains(localX, localY)) {
			m_contentPane.mouseMoved(m, localX - m_contentPane.getX(), localY - m_contentPane.getY(), delta);
			if (!m_contentPane.isMousedOver()) {
				m_contentPane.setMousedOver(true);
				m_contentPane.mouseEntered(m, localX - m_contentPane.getX(), localY - m_contentPane.getY());
			}
		} else if (m_contentPane.isMousedOver()) {
			m_contentPane.setMousedOver(false);
			m_contentPane.mouseExited(m, localX - m_contentPane.getX(), localY - m_contentPane.getY());
		}
	}
	@Override
	public void mouseWheelMoved(Mouse m, int localX, int localY, int dm) {
		if (m_contentPane.contains(localX, localY)) m_contentPane.mouseWheelMoved(m, 
				localX - m_contentPane.getX(), localY - m_contentPane.getY(), dm);
	}
	@Override
	public void mouseDelta(Mouse m, int localX, int localY, int deltaX, int deltaY) {
		if (m_contentPane.contains(localX, localY)) 
			m_contentPane.mouseDelta(m, 
					localX - m_contentPane.getX(), localY - m_contentPane.getY(), deltaX, deltaY);
		if (isMoving()) {
			logger.info("Delta: " + deltaX + " " + deltaY + " MousePos " + localX + " " + localY);
			setX(getX() + deltaX);
			setY(getY() + deltaY);
		}
	}
	@Override
	public void mouseButtonPressed(Mouse m, int localX, int localY, MouseButton button) {
		if (m_contentPane.contains(localX, localY)) 
			m_contentPane.mouseButtonPressed(m, localX - m_contentPane.getX(), localY - m_contentPane.getY(), button);
		else if (getWindowBarBounds() != null && getWindowBarBounds().contains(localX, localY)) {
			m_moving = true;
		}
	}
	@Override
	public void mouseButtonReleased(Mouse m, int localX, int localY, MouseButton button) {
		if (m_contentPane.contains(localX, localY)) m_contentPane.mouseButtonReleased(m, 
				localX - m_contentPane.getX(), localY - m_contentPane.getY(), button);
		if (isMoving()) m_moving = false;
	}
}
