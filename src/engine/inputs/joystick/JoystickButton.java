package engine.inputs.joystick;

import java.util.ArrayList;

public class JoystickButton implements JoystickListener {
	protected ArrayList<JoystickListener> m_listeners = new ArrayList<JoystickListener>();

	private int m_id;
	private String m_name;
	public boolean m_state;
	
	public JoystickButton(String name, int id) {
		m_name = name;
		m_id = id;
	}
	
	public void addListener(JoystickListener listener) {
		m_listeners.add(listener);
	}
	public void removeListener(JoystickListener listener) {
		m_listeners.remove(listener);
	}
	
	protected void tellListeners(JoystickEvent e) {
		for (JoystickListener l : m_listeners) {
			l.joystickEvent(e);
		}
	}
	
	public void setName(String name) { m_name = name; }
	public void setDown(boolean pressed) { m_state = pressed; }
	public boolean isDown() { return m_state; }
	public String getName() { return m_name; }
	public int getID() { return m_id; }
	
	public boolean equals(JoystickButton button) {
		return button.getID() == getID();
	}
	
	@Override
	public void joystickEvent(JoystickEvent e) {
		if (e instanceof JoystickButtonEvent) {
			setDown(((JoystickButtonEvent) e).isDown());
			tellListeners(e);
		}
	}
}
