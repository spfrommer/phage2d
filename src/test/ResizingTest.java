package test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.lwjgl.opengl.Display;

import engine.core.implementation.camera.base.ViewPort;
import engine.graphics.Renderer;
import engine.graphics.lwjgl.LWJGLKeyboard;
import engine.graphics.lwjgl.LWJGLMouse;
import engine.graphics.lwjgl.LWJGLRenderer;
import engine.gui.AnimationState;
import engine.gui.ThemeFactory;
import engine.gui.theme.Area;
import engine.inputs.keyboard.Keyboard;
import engine.inputs.mouse.Mouse;

public class ResizingTest {
	public static void main(String[] args) {
		LWJGLRenderer.instance().initDisplayWithoutCanvas(1024, 1024);
		// Graphics2D graphics = LWJGLGraphics2D.instance();
		Renderer renderer = LWJGLRenderer.instance();
		ViewPort fakePort = new ViewPort(null);
		fakePort.resized(1024, 1024);
		Mouse mouse = new LWJGLMouse(fakePort.getViewShape());

		Keyboard keyboard = LWJGLKeyboard.instance();

		Area r = null;
		try {
			r = ThemeFactory
					.createResizableFromFolder(new File(ResizingTest.class.getResource("/test/resize").toURI()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		while (!Display.isCloseRequested()) {
			r.render(renderer, new AnimationState(), 10, 10);
			LWJGLRenderer.instance().update(60);
		}
	}
}
