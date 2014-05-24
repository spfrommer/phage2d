package engine.inputs.joystick;

import java.util.ArrayList;

/*
 * Axes should be implemented as:
 * left to right = -1 to 1
 * front to back = -1 to 1
 */

public class Joystick {
	protected ArrayList<JoystickButton> m_buttons = new ArrayList<JoystickButton>();
	protected ArrayList<JoystickAxis> m_axes = new ArrayList<JoystickAxis>();
	protected ArrayList<JoystickListener> m_listeners = new ArrayList<JoystickListener>();
	
	
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
	
	public void addButton(JoystickButton button) {
		m_buttons.add(button);
	}
	
	public void removeButton(JoystickButton button) {
		m_buttons.remove(button);
	}
	
	public void addAxis(JoystickAxis axis) {
		m_axes.add(axis);
	}
	
	public void removeAxis(JoystickAxis axis) {
		m_axes.remove(axis);
	}
	
	public JoystickButton getButton(String name) {
		for (JoystickButton b : m_buttons) {
			if (b.getName().equals(name)) return b;
		}
		return null;
	}
	public JoystickButton getButton(int id) {
		for (JoystickButton b : m_buttons) {
			if (b.getID() == id) return b;
		}
		return null;
	}
	public JoystickAxis getAxis(String name) {
		for (JoystickAxis a : m_axes) {
			if (a.getName().equals(name)) return a;
		}
		return null;
	}
	public JoystickAxis getAxis(int id) {
		for (JoystickAxis a : m_axes) {
			if (a.getID() == id) return a;
		}
		return null;
	}
	
	protected void updateAxis(JoystickAxis axis, float value) {
		if (axis.getValue() != value) {
			JoystickEvent e = new JoystickAxisEvent(this, axis, value);
			axis.joystickEvent(e);
			tellListeners(e);
		}
	}
	protected void updateButton(JoystickButton b, boolean state) {
		if (b.isDown() != state) {
			JoystickEvent e = new JoystickButtonEvent(this, b, state);
			b.joystickEvent(e);
			tellListeners(e);
		}
	}
}
