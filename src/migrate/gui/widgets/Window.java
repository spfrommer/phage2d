package migrate.gui.widgets;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import migrate.gui.Dimension;
import migrate.gui.Widget;
import migrate.gui.layout.BorderLayout;
import migrate.input.Key;
import migrate.input.Keyboard;
import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;
import migrate.input.MouseListener;
import migrate.vector.Vector2f;
import engine.graphics.Renderer;

public abstract class Window extends Widget {
	//private static final Logger logger = LoggerFactory.getLogger(Window.class);
	
	private String m_title = null;
	private boolean m_resizeEnabled = true;
	private boolean m_movable = true;
	
	private ResizeContext m_resizeContext = null;
	private DragContext m_dragContext = null;
	
	private Panel m_contentPane = new Panel();
	
	public Window() {
		m_contentPane.setLayout(new BorderLayout());
	}
	
	
	public void setResizable(boolean resizable) { m_resizeEnabled = resizable; }
	public void setMovable(boolean movable) { m_movable = movable; }
	public void setTitle(String title) { m_title = title; }
	
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
	

	public boolean isMoving() { return m_dragContext != null; }
	public boolean isBeingResized() { return m_resizeContext != null; }
	
	public boolean isMovable() { return m_movable; }
	public boolean isResizable() { return m_resizeEnabled; }
	
	@Override
	public boolean hasFocusedChild() { 
		return super.hasFocusedChild() || m_contentPane.hasFocusedChild();
	}
	
	public String getTitle() { return m_title; }
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
		m_contentPane.setBounds(contentBounds);
		//Now pass the validate call to the contentPane
		m_contentPane.validate();
	}
	
	public void renderWidget(Renderer r) {
		renderFrame(r);
		m_contentPane.render(r);
	}
	public abstract void renderFrame(Renderer r);
	
	//Resize and drag functions
	
	public void doDrag(Mouse m) {
		m_dragContext = new DragContext(getX(), getY(), m.getX(), m.getY());
		//Add a listener that will listen for releases and movements not just inside the gui
		m.addListener(new MouseListener() {
			public void mouseMoved(Mouse m, int x, int y, Vector2f delta) {
				if (m_dragContext == null) return;
				//To compute final x, add change in mouse x to widing starting x
				int finalX = m_dragContext.getStartingXPos() + (x - m_dragContext.getStartingMouseX());
				//Same with y
				int finalY = m_dragContext.getStartingYPos() + (y - m_dragContext.getStartingMouseY());
				setX(finalX);
				setY(finalY);
			}
			public void mouseWheelMoved(Mouse m, int dm) {}
			public void mouseDelta(Mouse m, int dx, int dy) {}
			public void mouseButtonPressed(Mouse m, MouseButton button) {}
			public void mouseButtonReleased(Mouse m, MouseButton button) {
				m_dragContext = null;
				//Remove the listener
				m.removeListener(this);
			}
		});
	}
	public void doResize(Mouse m, Vector2f resizeDirection) {
		m_resizeContext = new ResizeContext(resizeDirection, getWidth(), getHeight(),
				getX(), getY(), m.getX(), m.getY());
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

				//Add the resizeAmount to the width and the height
				setWidth(startingDim.getWidth() + (int) resizeAmount.getX());
				setHeight(startingDim.getHeight() + (int) resizeAmount.getY());
				//Special exceptions if the window x and y need to be changed
				if (dragDir.getX() < 0) {
					//The x coordinate should change inversely
					//to the total change in width
					int finalX = m_resizeContext.getStartingX() - 
							(getWidth() - m_resizeContext.getStartingSize().getWidth());
					setX(finalX);
				}
				if (dragDir.getY() < 0) {
					int finalY = m_resizeContext.getStartingY() - 
							(getHeight() - m_resizeContext.getStartingSize().getHeight());
					setY(finalY);
				}
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
	
	//----Input Methods------
	/*
	 * A window will only redistribute a input call to its contentPane
	 */
	@Override
	public void keyPressed(Keyboard keyboard, Key key) {
		super.keyPressed(keyboard, key);
		if (m_contentPane.hasFocusedChild()) m_contentPane.keyPressed(keyboard, key);
	}
	@Override
	public void keyReleased(Keyboard keyboard, Key key) {
		super.keyReleased(keyboard, key);
		if (m_contentPane.hasFocusedChild()) m_contentPane.keyReleased(keyboard, key);
	}
	@Override
	public void mouseButtonPressed(Mouse m, int localX, int localY, MouseButton button) {
		super.mouseButtonPressed(m, localX, localY, button);
		if (m_contentPane.contains(localX, localY)) 
			m_contentPane.mouseButtonPressed(m, localX - m_contentPane.getX(), localY - m_contentPane.getY(), button);
		//Now check inside the window bar for dragging
		else if (isMovable() && getWindowBarBounds() != null && getWindowBarBounds().contains(localX, localY)) {
			doDrag(m);
		}
		//Now check for resizing
		if (isResizable()) {
			ResizeAreaMapping mapping = getResizeAreaMapping();
			if (mapping == null) return;
			for (Entry<Rectangle, Vector2f> entry : mapping.getMapping()) {
				if (entry.getKey().contains(localX, localY)) {
					doResize(m, entry.getValue());
				}
			}
		}
	}
	@Override
	public void mouseButtonReleased(Mouse m, int localX, int localY, MouseButton button) {
		super.mouseButtonReleased(m, localX, localY, button);
		if (m_contentPane.contains(localX, localY)) m_contentPane.mouseButtonReleased(m, 
				localX - m_contentPane.getX(), localY - m_contentPane.getY(), button);
	}
	@Override
	public void mouseMoved(Mouse m, int localX, int localY, Vector2f delta) {
		super.mouseMoved(m, localX, localY, delta);
		if (m_contentPane.contains(localX, localY)) {
			m_contentPane.mouseMoved(m, localX - m_contentPane.getX(), localY - m_contentPane.getY(), delta);
			if (!m_contentPane.isMousedOver()) {
				m_contentPane.mouseEntered(m, localX - m_contentPane.getX(), localY - m_contentPane.getY());
			}
		} else if (m_contentPane.isMousedOver()) {
			m_contentPane.mouseExited(m, localX - m_contentPane.getX(), localY - m_contentPane.getY());
		}
	}
	@Override
	public void mouseWheelMoved(Mouse m, int localX, int localY, int dm) {
		super.mouseWheelMoved(m, localX, localY, dm);
		if (m_contentPane.contains(localX, localY)) m_contentPane.mouseWheelMoved(m, 
				localX - m_contentPane.getX(), localY - m_contentPane.getY(), dm);
	}
	@Override
	public void mouseDelta(Mouse m, int localX, int localY, int deltaX, int deltaY) {
		super.mouseDelta(m, localX, localY, deltaX, deltaY);
		if (m_contentPane.contains(localX, localY)) 
			m_contentPane.mouseDelta(m, 
					localX - m_contentPane.getX(), localY - m_contentPane.getY(), deltaX, deltaY);
	}
	public static class ResizeAreaMapping {
		private HashMap<Rectangle, Vector2f> m_dragDirectionMap = new HashMap<Rectangle, Vector2f>();
		
		public void add(Rectangle r, Vector2f v) {
			m_dragDirectionMap.put(r, v);
		}
		public Set<Entry<Rectangle, Vector2f>> getMapping() { return m_dragDirectionMap.entrySet(); }
	}
	public static class ResizeContext {
		private final Vector2f m_dragDirection;
		private final Dimension m_startingSize;
		private final int m_startingX;
		private final int m_startingY;
		//Mouse starting positions are in display coordinates, not local
		private final int m_startingMouseX;
		private final int m_startingMouseY;
		public ResizeContext(Vector2f direction, int sWidth, int sHeight,
													int sX, int sY,
													int mX, int mY) {
			m_dragDirection = direction;
			m_startingSize = new Dimension(sWidth, sHeight);
			m_startingX = sX;
			m_startingY = sY;
			m_startingMouseX = mX;
			m_startingMouseY = mY;
		}
		public Vector2f getDragDirection() { return m_dragDirection; }
		public Dimension getStartingSize() { return m_startingSize; }
		public int getStartingX() { return m_startingX; }
		public int getStartingY() { return m_startingY; }
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
		public int getStartingXPos() { return m_startingX; }
		public int getStartingYPos() { return m_startingY; }
		public int getStartingMouseX() { return m_startingMouseX; }
		public int getStartingMouseY() { return m_startingMouseY; }
	}
}
