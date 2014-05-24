package engine.inputs.joystick;

import java.util.ArrayList;

public class JoystickAxis implements JoystickListener {
	protected ArrayList<JoystickListener> m_listeners = new ArrayList<JoystickListener>();
	private int m_id;
	private String m_name;
	public float m_value;
	
	public JoystickAxis(String name, int id) {
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
	public void setValue(float value) { m_value = value; }
	public float getValue() { return m_value; }
	public String getName() { return m_name; }
	public int getID() { return m_id; }
	
	public boolean equals(JoystickAxis button) {
		return button.getID() == getID();
	}

	@Override
	public void joystickEvent(JoystickEvent e) {
		if (e instanceof JoystickAxisEvent) {
			setValue(((JoystickAxisEvent) e).getValue());
			tellListeners(e);
		}
	}
	
	
}
