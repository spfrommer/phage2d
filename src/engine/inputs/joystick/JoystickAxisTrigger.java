package engine.inputs.joystick;

import engine.inputs.InputTrigger;

public class JoystickAxisTrigger extends InputTrigger implements JoystickListener {
	
	public JoystickAxisTrigger() {}
	public JoystickAxisTrigger(JoystickAxis axis) {
		axis.addListener(this);
	}
	
	@Override
	public void joystickEvent(JoystickEvent e) {
		if (e instanceof JoystickAxisEvent) {
			trigger(((JoystickAxisEvent) e).getValue());
		}
	}
	
}
