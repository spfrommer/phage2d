package migrate.gui.widgets;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import migrate.gui.Dimension;
import migrate.gui.Widget;
import migrate.gui.layout.BorderLayout;
import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;
import migrate.input.MouseListener;
import migrate.vector.Vector2f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import engine.graphics.Renderer;

public abstract class Window extends Widget {
	//private static final Logger logger = LoggerFactory.getLogger(Window.class);
	
	private boolean m_moving = false;
	private boolean m_resizeEnabled = true;
	private ResizeContext m_resizeContext = null;
	
	private Panel m_contentPane = new Panel();
	
	public Window() {
		m_contentPane.setLayout(new BorderLayout());
	}
	
	public void setResizable(boolean resizable) { m_resizeEnabled = resizable; }
	
	@Override
	public void setWidth(int width) {
		//If size changes, send the validate call again
		//We need to revalidate after the width has changed
		//but we only know whether it has changed
		//before we set it
		boolean revalidate = width != getWidth(); 
		super.setWidth(width);
		if (revalidate) validate();
	}
	@Override
	public void setHeight(int height) {
		boolean revalidate = height != getHeight();
		super.setHeight(height);
		if (revalidate) validate();
	}
	

	public boolean isMoving() { return m_moving; }
	public boolean isResizable() { return m_resizeEnabled; }
	
	public Panel getContentPane() { return m_contentPane; }
	
	protected abstract Rectangle getContentPaneBounds();
	protected abstract Rectangle getWindowBarBounds();
	protected abstract ResizeAreaMapping getResizeAreaMapping();
	
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
	
	@Override
	public void validate() {
		//Update the content pane bounds
		Rectangle contentBounds = getContentPaneBounds();
		m_contentPane.setX((int) contentBounds.getX());
		m_contentPane.setY((int) contentBounds.getY());
		m_contentPane.setWidth((int) contentBounds.getWidth());
		m_contentPane.setHeight((int) contentBounds.getHeight());
		//Now pass the validate call to the contentPane
		m_contentPane.validate();
	}
	
	public void renderWidget(Renderer r) {
		renderFrame(r);
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
					m_resizeContext = new ResizeContext(entry.getValue(), getWidth(), getHeight(),
															m.getX(), m.getY());
					//Add a listener so we will be notified if the mouse moves, even
					//if it moves out of the window
					m.addListener(new MouseListener() {
						public void mouseMoved(Mouse m, int x, int y, Vector2f delta) {
							if (m_resizeContext == null) return;
							Vector2f startingMouse = new Vector2f(m_resizeContext.getStartingMouseX(),
																m_resizeContext.getStartingMouseY());
							Vector2f dragDir = m_resizeContext.getDragDirection();
							Dimension startingDim = m_resizeContext.getStartingSize();

							Vector2f totalDelta = Vector2f.sub(new Vector2f(x, y), startingMouse, null);
							
							Vector2f resizeAmount = totalDelta.scale(dragDir, null);
							//logger.debug("DragDir:" + dragDir);
							//logger.debug("Delta:" + delta);
							//logger.debug("Resize Amount: " + resizeAmount);
							int finalWidth = startingDim.getWidth() + (int) resizeAmount.getX();
							int finalHeight = startingDim.getHeight() + (int) resizeAmount.getY();
							/*if (dragDir.getX() < 0) {
							  setX(getX() - (int) resizeAmount.getX());
							}
							if (dragDir.getY() < 0) {
								setY(getY() - (int) resizeAmount.getY());
							}
							setWidth(getWidth() + (int) resizeAmount.getX());
							setHeight(getHeight() + (int) resizeAmount.getY());*/
							setWidth(finalWidth);
							setHeight(finalHeight);
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
		private Dimension m_startingSize;
		//Mouse starting positions are in display coordinates, not local
		private int m_startingMouseX;
		private int m_startingMouseY;
		public ResizeContext(Vector2f direction, int sWidth, int sHeight, int mx, int my) {
			m_dragDirection = direction;
			m_startingSize = new Dimension(sWidth, sHeight);
			m_startingMouseX = mx;
			m_startingMouseY = my;
		}
		public Vector2f getDragDirection() { return m_dragDirection; }
		public Dimension getStartingSize() { return m_startingSize; }
		public int getStartingMouseX() { return m_startingMouseX; }
		public int getStartingMouseY() { return m_startingMouseY; }
	}
	public static class DragContext {
		private int m_startingX;
		private int m_startingY;
		//Mouse starting positions are in display coordinates, not local
		private int m_startingMouseX;
		private int m_startingMouseY;
		public DragContext(int wX, int wY, int mx, int my) {
			m_startingX = wX;
			m_startingY = wY;
			m_startingMouseX = mx;
			m_startingMouseY = my;
		}
		public int getStartingX() { return m_startingX; }
		public int getStartingY() { return m_startingY; }
		public int getStartingMouseX() { return m_startingMouseX; }
		public int getStartingMouseY() { return m_startingMouseY; }
	}
}
