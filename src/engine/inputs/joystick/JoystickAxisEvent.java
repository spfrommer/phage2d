package engine.inputs.joystick;

public class JoystickAxisEvent extends JoystickEvent {
	private JoystickAxis m_axis;
	private float m_value;
	
	public JoystickAxisEvent(Joystick joystick, JoystickAxis axis, float value) {
		super(joystick);
		m_axis = axis;
		m_value = value;
	}
	public JoystickAxis getAxis() { return m_axis; }
	public float getValue() { return m_value; }
	
	@Override
	public String toString() {
		return "Axis: " + getAxis().getName() + ": " + getValue();
	}
	
	public JoystickEvent copy() {
		return new JoystickAxisEvent(getJoystick(), getAxis(), getValue());
	}
}
