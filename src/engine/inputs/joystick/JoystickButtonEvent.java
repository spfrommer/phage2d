package engine.inputs.joystick;

public class JoystickButtonEvent extends JoystickEvent {
	private JoystickButton m_button;
	private boolean m_down;
	
	public JoystickButtonEvent(Joystick joystick, JoystickButton button, boolean down) {
		super(joystick);
		m_button = button;
		m_down = down;
	}
	
	public JoystickButton getButton() { return m_button; }
	public boolean isDown() { return m_down; }
	
	public String toString() {
		return "Button: " + getButton().getName() + ": " + isDown();
	}
	
	public JoystickEvent copy() {
		return new JoystickButtonEvent(getJoystick(), getButton(), isDown());
	}
	
}
