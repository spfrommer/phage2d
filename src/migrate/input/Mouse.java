package migrate.input;

import java.util.ArrayList;
import java.util.HashMap;

import migrate.vector.Vector2f;

public abstract class Mouse {
	protected int m_x = 0;
	protected int m_y = 0;
	
	private HashMap<MouseButton, Boolean> m_buttonStates = new HashMap<MouseButton, Boolean>();
	private HashMap<Integer, MouseButton> m_buttonIds = new HashMap<Integer, MouseButton>();
	private HashMap<String, MouseButton> m_buttonNames = new HashMap<String, MouseButton>();
	
	private ArrayList<MouseListener> m_listeners = new ArrayList<MouseListener>();
	
	public Mouse() {}
	
	public void add(MouseButton b) {
		m_buttonIds.put(b.getId(), b);
		m_buttonNames.put(b.getName(), b);
		
		m_buttonStates.put(b, false);
	}
	public void addListener(MouseListener l) {
		synchronized(m_listeners) {
			m_listeners.add(l);
		}
	}
	public void removeListener(MouseListener l) {
		synchronized(m_listeners) {
			m_listeners.remove(l);
		}
	}
	public MouseButton getMouseButton(String name) {
		return m_buttonNames.get(name);
	}
	public MouseButton getMouseButton(int id) {
		return m_buttonIds.get(id);
	}
	
	public int getX() { return m_x; }
	public int getY() { return m_y; }
	
	protected void updateButtonState(MouseButton button, boolean state) {
		m_buttonStates.put(button, state);
		for(int i = 0; i < m_listeners.size(); i++) {
			MouseListener l = m_listeners.get(i);
			if (state) l.mouseButtonPressed(this, button);
			else l.mouseButtonReleased(this, button);
		}
	}
	protected void updateMousePosition(int x, int y) {
		if (x != m_x || m_y != y) {
			Vector2f delta = new Vector2f(x - m_x, y - m_y);
			m_x = x;
			m_y = y;
			for(int i = 0; i < m_listeners.size(); i++) {
				MouseListener l = m_listeners.get(i);
				l.mouseMoved(this, x, y, delta);
			}
		}
	}
	//Note: Delta might not be the same as x - prex, y - prey if the mouse is at the edge of the screen
	protected void updateDelta(int x, int y) {
		if (x != 0 || y != 0) {
			for(int i = 0; i < m_listeners.size(); i++) {
				MouseListener l = m_listeners.get(i);
				l.mouseDelta(this, x, y);
			}
		}
	}
	protected void mouseWheelMoved(int dm) {
		synchronized(m_listeners) {
			for(int i = 0; i < m_listeners.size(); i++) {
				MouseListener l = m_listeners.get(i);
				l.mouseWheelMoved(this, dm);
			}
		}
	}
	//-----Abstract functions------

	public abstract void poll();
	
	public static class MouseButton {
		public static final String LEFT_BUTTON_NAME = "LEFT_BUTTON";
		public static final String RIGHT_BUTTON_NAME = "RIGHT_BUTTON";
		public static final String MIDDLE_BUTTON_NAME = "MIDDLE_BUTTON";
	
		private final int m_id;
		private final String m_name;
		
		public MouseButton(int id, String name) {
			m_id = id;
			m_name = name;
		}
		
		public int getId() { return m_id;	}
		public String getName() { return m_name; }
	}
	
	
	//------------Triggers-----------
	
	
	public static class MouseButtonTrigger extends InputTrigger implements MouseListener {
		private final MouseButton m_button;
		
		public MouseButtonTrigger(Mouse mouse, MouseButton button) {
			mouse.addListener(this);
			m_button = button;
		}
		@Override
		public void mouseMoved(Mouse m, int x, int y, Vector2f delta) {}
		@Override
		public void mouseWheelMoved(Mouse m, int dm) {}
		@Override
		public void mouseDelta(Mouse m, int x, int y) {}
		@Override
		public void mouseButtonPressed(Mouse m, MouseButton button) {
			if (button.equals(m_button)) trigger(1f);
		}
		@Override
		public void mouseButtonReleased(Mouse m, MouseButton button) {
			if (button.equals(m_button)) trigger(0f);
		}
	}
	
	public static class MouseXTrigger extends InputTrigger implements MouseListener {		
		public MouseXTrigger(Mouse mouse) {
			mouse.addListener(this);
		}
		@Override
		public void mouseMoved(Mouse m, int x, int y, Vector2f delta) {
			trigger(x);
		}
		@Override
		public void mouseWheelMoved(Mouse m, int dm) {}
		@Override
		public void mouseDelta(Mouse m, int x, int y) {}
		@Override
		public void mouseButtonPressed(Mouse m, MouseButton button) {}
		@Override
		public void mouseButtonReleased(Mouse m, MouseButton button) {}
	}
	public static class MouseYTrigger extends InputTrigger implements MouseListener {
		public MouseYTrigger(Mouse mouse) {
			mouse.addListener(this);
		}
		@Override
		public void mouseMoved(Mouse m, int x, int y, Vector2f delta) {
			trigger(y);
		}
		@Override
		public void mouseWheelMoved(Mouse m, int dm) {}
		@Override
		public void mouseDelta(Mouse m, int x, int y) {}
		@Override
		public void mouseButtonPressed(Mouse m, MouseButton button) {}
		@Override
		public void mouseButtonReleased(Mouse m, MouseButton button) {}
	}
}
