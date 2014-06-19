package engine.graphics.lwjgl;

import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;

import engine.inputs.mouse.Mouse;
import engine.inputs.mouse.MouseButton;

public class LWJGLMouse extends Mouse {
	public static int POLL_INTERVAL = 10;

	{
		MouseButton left = new MouseButton(MouseButton.LEFT_NAME, 0);
		MouseButton right = new MouseButton(MouseButton.RIGHT_NAME, 1);
		MouseButton middle = new MouseButton(MouseButton.MIDDLE_NAME, 2);
		addMouseButton(left);
		addMouseButton(right);
		addMouseButton(middle);
	}

	public Rectangle m_viewRect;

	public LWJGLMouse(Rectangle viewRect) {
		m_viewRect = viewRect;
		EventLoop.instance().attach(this);
	}

	public Rectangle getViewRect() {
		return m_viewRect;
	}

	public static class EventLoop implements Runnable {
		private static EventLoop s_instance = null;

		private Thread m_thread;

		private ArrayList<LWJGLMouse> m_mice = new ArrayList<LWJGLMouse>();

		private EventLoop() {
		}

		public void attach(LWJGLMouse mouse) {
			// Thread.dumpStack();
			m_mice.add(mouse);
			start();
		}

		public void start() {
			if (m_thread == null) {
				m_thread = new Thread(this);
				m_thread.setName("LWJGLMouseEventLoop");
				m_thread.start();
			} else if (!m_thread.isAlive()) {
				m_thread.start();
			}
		}

		public void notifyPositionChange(int x, int y) {
			for (LWJGLMouse mouse : m_mice) {
				// System.out.println("Updating to: " + x + ", " + y);
				// We need to flip the y to the top, so subtract it from the height
				mouse.mouseMoved(x, (int) mouse.getViewRect().getHeight() - y);
				// System.out.println("Updating mouse pos for " + mouse + " to: " + x + " " +
				// (mouse.getViewPort().getHeight() - y));
			}
		}

		public void notifyButtonStateChange(int id, boolean value) {
			for (LWJGLMouse mouse : m_mice) {
				MouseButton button = mouse.getMouseButton(id);
				mouse.setButtonState(button, value);
			}
		}

		@Override
		public void run() {
			// Grab the event thread lock
			synchronized (this) {
				while (true) {
					if (org.lwjgl.input.Mouse.isCreated()) {
						// In here goes event code
						org.lwjgl.input.Mouse.poll();

						if (org.lwjgl.input.Mouse.next()) {
							int button = org.lwjgl.input.Mouse.getEventButton();
							if (button != -1) {
								notifyButtonStateChange(button, org.lwjgl.input.Mouse.getEventButtonState());
							} else {
								int x = org.lwjgl.input.Mouse.getEventX();
								int y = org.lwjgl.input.Mouse.getEventY();
								notifyPositionChange(x, y);
								// System.out.println("Position Changed: " + x + " " + y);
							}
						}
						//
					} else if (org.lwjgl.opengl.Display.isCreated()) {
						try {
							org.lwjgl.input.Mouse.create();
						} catch (LWJGLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		public static EventLoop instance() {
			if (s_instance == null) {
				s_instance = new EventLoop();
			}
			return s_instance;
		}
	}
}
