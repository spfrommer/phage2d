package engine.inputs.joystick;

import engine.inputs.InputEvent;

public abstract class JoystickEvent extends InputEvent {
	private Joystick m_joystick;
	
	public JoystickEvent(Joystick joystick) {
		m_joystick = joystick;
	}
	public Joystick getJoystick() { return m_joystick; }
	
	
	public abstract JoystickEvent copy(); 
}
