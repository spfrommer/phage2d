package migrate;

import java.awt.Rectangle;
import java.util.ArrayList;

import migrate.input.Keyboard;
import migrate.input.LWJGLKeyboard;
import migrate.input.LWJGLMouse;
import migrate.input.Mouse;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.DisplayMode;

import engine.core.implementation.camera.base.ResizeListener;
import engine.graphics.Renderer;
import engine.graphics.lwjgl.LWJGLRenderer;

public class LWJGLDisplay implements Display {
	/**
	 * A lock that will be grabbed whenever display-related functions need
	 * to fiddle around with the display, or require the display
	 */
	private static boolean s_created = false;

	private final LWJGLMouse m_mouse = LWJGLMouse.getInstance();
	private final LWJGLKeyboard m_keyboard = LWJGLKeyboard.getInstance();
	
	private final boolean m_vsync;
	private final int m_width;
	private final int m_height;
	
	private ArrayList<ResizeListener> m_listeners = new ArrayList<ResizeListener>();
	
	public LWJGLDisplay(int width, int height, boolean vsync) {
		m_width = width;
		m_height = height;
		m_vsync = vsync;
		m_mouse.setDisplayShape(new Rectangle(0, 0, m_width, m_height));
	}

	@Override
	public int getWidth() { return m_width; }
	@Override
	public int getHeight() { return m_height; }

	public Mouse getMouse() { return m_mouse; }
	public Keyboard getKeyboard() { return m_keyboard; }
	public Renderer getRenderer() { return LWJGLRenderer.getInstance(); }

	public void setTitle(String title) { 
		org.lwjgl.opengl.Display.setTitle(title);
	}
	public String getTitle() { 
		return org.lwjgl.opengl.Display.getTitle();
	}

	@Override
	public void addResizedListener(ResizeListener l) {
		m_listeners.add(l);
		l.resized(m_width, m_height);
	}
	@Override
	public void init() {
		try {
			org.lwjgl.opengl.Display.setDisplayMode(new DisplayMode(m_width, m_height));
			org.lwjgl.opengl.Display.setVSyncEnabled(m_vsync);
			org.lwjgl.opengl.Display.create();
			s_created = true;
		} catch (LWJGLException e) {
			throw new RuntimeException("Failed to spawn display", e);
		}
	}

	@Override
	public void destroy() {
		org.lwjgl.opengl.Display.destroy();
	}

	@Override
	public boolean closeRequested() {
		if (s_created) return org.lwjgl.opengl.Display.isCloseRequested();
		else return false;
	}

	@Override
	public void update(int fps) {
		if (s_created) {
			org.lwjgl.opengl.Display.update();
			org.lwjgl.opengl.Display.sync(fps);
		}
	}
	
	public static boolean isCreated() { return s_created; }
}
