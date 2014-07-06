package migrate.gui.widgets;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import migrate.gui.Dimension;
import migrate.gui.Widget;
import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;
import migrate.input.MouseListener;
import migrate.vector.Vector2f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import engine.graphics.Renderer;

public abstract class Window extends Widget {
	private static final Logger logger = LoggerFactory.getLogger(Window.class);
	
	private boolean m_moving = false;
	private boolean m_resizeEnabled = true;
	private ResizeContext m_resizeContext = null;
	
	private Panel m_contentPane = new Panel();
	
	public Window() {
		
	}
	
	public void setResizable(boolean resizable) { m_resizeEnabled = resizable; }
	
	public Panel getContentPane() { return m_contentPane; }
	
	public boolean isMoving() { return m_moving; }
	public boolean isResizable() { return m_resizeEnabled; }
	
	protected abstract Rectangle getContentPaneBounds();
	protected abstract Rectangle getWindowBarBounds();
	protected abstract ResizeAreaMapping getResizeAreaMapping();
	
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

	/**
	 * If the minsize has not been set(is null),
	 * then the minimum size will be calculated
	 * by subtracting the contentpanebound's width and height from
	 * the window's width and height, effectively calculating the
	 * dimensions of the non-content area
	 */
	@Override
	public Dimension getMinSize() {
		Rectangle contentBounds = getContentPaneBounds();
		return new Dimension(getWidth() - (int) contentBounds.getWidth(), 
							getHeight() - (int) contentBounds.getHeight());
	}
	
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
	}
	@Override
	public void mouseButtonPressed(Mouse m, int localX, int localY, MouseButton button) {
		if (m_contentPane.contains(localX, localY)) 
			m_contentPane.mouseButtonPressed(m, localX - m_contentPane.getX(), localY - m_contentPane.getY(), button);
		else if (getWindowBarBounds() != null && getWindowBarBounds().contains(localX, localY)) {
			m_moving = true;
			//Add a listener that will listen for releases and movements not just inside the gui
			m.addListener(new MouseListener() {
				public void mouseMoved(Mouse m, int x, int y, Vector2f delta) {
					if (isMoving()) {
						//logger.info("Delta: " + delta.getX() + " " + delta.getY() + " MousePos " + x + " " + y);
						setX(getX() + (int) delta.getX());
						setY(getY() + (int) delta.getY());
					}
				}
				public void mouseWheelMoved(Mouse m, int dm) {}
				public void mouseDelta(Mouse m, int dx, int dy) {}
				public void mouseButtonPressed(Mouse m, MouseButton button) {}
				public void mouseButtonReleased(Mouse m, MouseButton button) {
					//Stop moving
					m_moving = false;
					//Remove the listener
					m.removeListener(this);
				}
			});
		}
		if (m_resizeEnabled) {
			ResizeAreaMapping mapping = getResizeAreaMapping();
			for (Entry<Rectangle, Vector2f> entry : mapping.getMapping()) {
				if (entry.getKey().contains(localX, localY)) {
					m_resizeContext = new ResizeContext(entry.getValue());
					//Add a listener so we will be notified if the mouse moves, even
					//if it moves out of the window
					m.addListener(new MouseListener() {
						public void mouseMoved(Mouse m, int x, int y, Vector2f delta) {
							if (m_resizeContext == null) return;
							Vector2f dragDir = m_resizeContext.getDragDirection();
							Vector2f resizeAmount = delta.scale(dragDir, null);
							//logger.debug("DragDir:" + dragDir);
							//logger.debug("Delta:" + delta);
							//logger.debug("Resize Amount: " + resizeAmount);
							if (dragDir.getX() < 0) {
								setX(getX() + (int) resizeAmount.getX());
							}
							setWidth(getWidth() + (int) resizeAmount.getX());
							setHeight(getHeight() + (int) resizeAmount.getY());
						}
						public void mouseWheelMoved(Mouse m, int dm) {}
						public void mouseDelta(Mouse m, int dx, int dy) {}
						public void mouseButtonPressed(Mouse m, MouseButton button) {}
						public void mouseButtonReleased(Mouse m, MouseButton button) {
							//Stop resizing
							m_resizeContext = null;
							//Remove the listener
							m.removeListener(this);
						}
					});
				}
			}
		}
	}
	@Override
	public void mouseButtonReleased(Mouse m, int localX, int localY, MouseButton button) {
		if (m_contentPane.contains(localX, localY)) m_contentPane.mouseButtonReleased(m, 
				localX - m_contentPane.getX(), localY - m_contentPane.getY(), button);
	}
	
	public static class ResizeAreaMapping {
		private HashMap<Rectangle, Vector2f> m_dragDirectionMap = new HashMap<Rectangle, Vector2f>();
		
		public void add(Rectangle r, Vector2f v) {
			m_dragDirectionMap.put(r, v);
		}
		public Set<Entry<Rectangle, Vector2f>> getMapping() { return m_dragDirectionMap.entrySet(); }
	}
	public static class ResizeContext {
		private Vector2f m_dragDirection;
		public ResizeContext(Vector2f direction) {
			m_dragDirection = direction;
		}
		public Vector2f getDragDirection() { return m_dragDirection; }
	}
}
