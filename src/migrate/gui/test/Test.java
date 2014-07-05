package migrate.gui.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import migrate.LWJGLDisplay;
import migrate.gui.GUI;
import migrate.gui.widgets.Button;
import migrate.input.Keyboard;
import migrate.input.LWJGLKeyboard;
import migrate.input.LWJGLMouse;
import migrate.input.Mouse;

import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.GUITest;
import engine.graphics.lwjgl.LWJGLRenderer;

public class Test {
	private static Logger logger = LoggerFactory.getLogger(GUITest.class);
	
	private LWJGLDisplay m_display;
	
	public void run() {
		m_display = new LWJGLDisplay(1024, 1024, true);
		//m_display.init();
		LWJGLRenderer.initDisplayWithoutCanvas(1024, 1024, true);
		
		Mouse mouse = LWJGLMouse.getInstance();
		Keyboard keyboard = LWJGLKeyboard.getInstance();
		
		GUI gui = new GUI();
		Button button = new SimpleButton();
		button.setX(100);
		button.setY(100);
		button.setWidth(100);
		button.setHeight(100);
		gui.add(button);
		
		mouse.addListener(gui);
		keyboard.addListener(gui);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Button pressed!");
			}
		});
		
		logger.debug("Initialization finished!");
		
		while(!m_display.closeRequested()) {
			gui.render(m_display.getRenderer());
			mouse.poll();
			keyboard.poll();
			LWJGLRenderer.getInstance().update();
			Display.sync(60);
			//m_display.update(60);
		}
		System.exit(0);
	}
	
	public static void main(String[] args) {
		new Test().run();
	}
}
