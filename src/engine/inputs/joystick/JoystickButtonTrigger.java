package engine.inputs.joystick;

import engine.inputs.InputTrigger;

public class JoystickButtonTrigger extends InputTrigger implements JoystickListener {
	public JoystickButtonTrigger() {}
	
	public JoystickButtonTrigger(JoystickButton button) {
		button.addListener(this);
	}
	
	public void joystickEvent(JoystickEvent e) {
		if (e instanceof JoystickButtonEvent) {
			boolean down = ((JoystickButtonEvent) e).isDown();
			if (down) trigger(1f);
			else trigger(0f);
		}
	}
}
