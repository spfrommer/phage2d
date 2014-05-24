package engine.graphics.lwjgl;

import org.lwjgl.input.Controller;

import engine.inputs.joystick.Joystick;
import engine.inputs.joystick.JoystickAxis;
import engine.inputs.joystick.JoystickButton;

public class LWJGLJoystick extends Joystick implements Runnable {
	private Thread m_thread = new Thread(this);
	private Controller m_controller;
	
	public LWJGLJoystick(Controller c) {
		m_controller = c;
	}
	
	public void start() {
		m_thread.start();
	}
	public void stop() {
		m_thread.interrupt();
	}
	
	public void initialize() {
		m_axes.clear();
		m_buttons.clear();
		
		for (int i = 0; i < m_controller.getAxisCount(); i++) {
			String name = m_controller.getAxisName(i);
			int id = i;
			JoystickAxis axis = new JoystickAxis(name, id);
			addAxis(axis);
		}
		for (int i= 0; i < m_controller.getButtonCount(); i++) {
			String name = m_controller.getButtonName(i);
			int id = i;
			JoystickButton button = new JoystickButton(name, id);
			addButton(button);
		}
	}
	
	public void poll() {
		m_controller.poll();
		for (int i = 0; i < m_controller.getAxisCount(); i++) {
			JoystickAxis axis = getAxis(i);
			updateAxis(axis, m_controller.getAxisValue(axis.getID()));
		}
		for (int i= 0; i < m_controller.getButtonCount(); i++) {
			JoystickButton button = getButton(i);
			updateButton(button, m_controller.isButtonPressed(i));
		}
	}
	
	public void run() {
		while (true) {
			poll();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}
