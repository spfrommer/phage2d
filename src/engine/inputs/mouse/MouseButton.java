package engine.inputs.mouse;

import java.util.ArrayList;

public class MouseButton implements MouseListener {
	public static String LEFT_NAME = "Left";
	public static String RIGHT_NAME = "Right";
	public static String MIDDLE_NAME = "Middle";
	
	private ArrayList<MouseListener> m_listeners = new ArrayList<MouseListener>();
	
	private int m_id;
	private String m_name;
	public boolean m_state;
	
	public MouseButton(String name, int id) {
		m_name = name;
		m_id = id;
	}
	
	public void addListener(MouseListener l) {
		m_listeners.add(l);
	}
	public void removeListener(MouseListener l) {
		m_listeners.remove(l);
	}
	
	public void setName(String name) { m_name = name; }
	public void setDown(boolean pressed) { m_state = pressed; }
	public boolean isDown() { return m_state; }
	public String getName() { return m_name; }
	public int getID() { return m_id; }
	
	public void mouseEvent(MouseEvent event) {
		if (event instanceof MouseButtonEvent) {
			if (((MouseButtonEvent) event).getButton().equals(this)) {
				setDown(((MouseButtonEvent) event).isDown());

				for (MouseListener listener : m_listeners) {
					listener.mouseEvent(event);
				}
			}
		}
	}
	
	public boolean equals(MouseButton button) {
		return button.getID() == getID();
	}
}
