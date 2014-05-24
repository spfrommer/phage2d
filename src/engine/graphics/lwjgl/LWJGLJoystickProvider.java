package engine.graphics.lwjgl;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

import engine.inputs.joystick.JoystickEvent;
import engine.inputs.joystick.JoystickListener;

public class LWJGLJoystickProvider {
	public ArrayList<LWJGLJoystick> m_joysticks = new ArrayList<LWJGLJoystick>();
	
	public void populateControllers() {
		try {
			Controllers.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			return;
		}
		Controllers.poll();
		
		m_joysticks.clear();
		
		for (int i = 0; i < Controllers.getControllerCount(); i++) {
			Controller c = Controllers.getController(i);
			LWJGLJoystick joystick = new LWJGLJoystick(c);
			m_joysticks.add(joystick);
			joystick.initialize();
			joystick.start();
		}
	}
	
	public ArrayList<LWJGLJoystick> getJoysticks() { 
		return m_joysticks;
	}
	
	public static void main(String[] args) {
		LWJGLJoystickProvider p = new LWJGLJoystickProvider();
		p.populateControllers();
		ArrayList<LWJGLJoystick> joys = p.getJoysticks();
		joys.get(0).addListener(new JoystickListener() {
			@Override
			public void joystickEvent(JoystickEvent e) {
				System.out.println(e);
			}
		});
	}
}
