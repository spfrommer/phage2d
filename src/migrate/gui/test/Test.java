package migrate.gui.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import migrate.LWJGLDisplay;
import migrate.gui.GUI;
import migrate.gui.widgets.Button;
import migrate.gui.widgets.Label;
import migrate.gui.widgets.Window;
import migrate.input.Keyboard;
import migrate.input.LWJGLKeyboard;
import migrate.input.LWJGLMouse;
import migrate.input.Mouse;

import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.GUITest;
import engine.graphics.lwjgl.LWJGLRenderer;
import engine.gui.layout.BorderLayout;

public class Test {
	private static Logger logger = LoggerFactory.getLogger(GUITest.class);
	
	private LWJGLDisplay m_display;
	
	public void run() {
		m_display = new LWJGLDisplay(1024, 1024, true);
		m_display.init();
		LWJGLRenderer.initDisplay(m_display.getWidth(), m_display.getHeight());
		//LWJGLRenderer.initDisplayWithoutCanvas(1024, 1024, true);
		
		Mouse mouse = LWJGLMouse.getInstance();
		Keyboard keyboard = LWJGLKeyboard.getInstance();
		
		GUI gui = new GUI();
		
		Window window = new SimpleWindow();
		window.setX(100);
		window.setY(100);
		window.setWidth(500);
		window.setHeight(500);
		Button button = new SimpleButton();
		button.setWidth(100);
		button.setHeight(100);
		//Button button2 = new SimpleButton();
		//button2.setX(0);
		//button2.setY(150);
		//button2.setWidth(50);
		//button2.setHeight(50);
		Label label = new SimpleLabel();
		label.setText("Foo + bar = Foobar");
		button.setContent(label);
		//window.getContentPane().setLayout(null);
		//window.getContentPane().add(label, BorderLayout.NORTH);
		window.getContentPane().add(button, BorderLayout.CENTER);
		window.setTitle("Really Long Test Title");
		window.validate();
		
		gui.add(window);
		
		mouse.addListener(gui);
		keyboard.addListener(gui);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Button pressed");
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
		m_display.destroy();
	}
	
	public static void main(String[] args) {
		new Test().run();
	}
}
