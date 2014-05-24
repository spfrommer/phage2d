package test;

import engine.graphics.lwjgl.LWJGLKeyboard;
import engine.graphics.lwjgl.LWJGLRenderer;
import engine.inputs.InputManager;

public class InputManagerTest {
	public static void main(String[] args) {
		LWJGLRenderer.initDisplayWithoutCanvas(1024, 1024);
		
		LWJGLKeyboard keyboard = LWJGLKeyboard.instance();
		
		InputManager manager = new InputManager();

		while (!org.lwjgl.opengl.Display.isCloseRequested()) {
			LWJGLRenderer.instance().update(60);
		}
	}
}
