package engine.graphics.lwjgl;

import java.util.ArrayList;

import engine.core.implementation.camera.base.Display;
import engine.core.implementation.camera.base.ResizeListener;
import engine.graphics.Renderable;
import engine.graphics.Renderer;

public class LWJGLDisplay implements Display {
	private final int m_width;
	private final int m_height;
	private final boolean m_sync;

	private ArrayList<ResizeListener> m_listeners = new ArrayList<ResizeListener>();
	private ArrayList<Renderable> m_renderables = new ArrayList<Renderable>();

	public LWJGLDisplay(int width, int height, boolean sync) {
		m_width = width;
		m_height = height;
		m_sync = sync;
	}

	@Override
	public int getWidth() {
		return m_width;
	}

	@Override
	public int getHeight() {
		return m_height;
	}

	@Override
	public void addResizedListener(ResizeListener l) {
		m_listeners.add(l);
		l.resized(m_width, m_height);
	}

	@Override
	public void addRenderable(Renderable r) {
		m_renderables.add(r);
	}

	@Override
	public void removeRenderable(Renderable r) {
		m_renderables.remove(r);
	}

	@Override
	public void init() {
		LWJGLRenderer.initDisplayWithoutCanvas(m_width, m_height, m_sync);
	}

	@Override
	public void destroy() {
		LWJGLRenderer.destroyDisplay();
	}

	@Override
	public boolean destroyRequested() {
		return org.lwjgl.opengl.Display.isCloseRequested();
	}

	@Override
	public Renderer getRenderer() {
		return LWJGLRenderer.getInstance();
	}

	@Override
	public void update() {
		LWJGLRenderer.getInstance().update();
	}

	@Override
	public void update(int fps) {
		LWJGLRenderer.getInstance().update();
		org.lwjgl.opengl.Display.sync(fps);
	}

	@Override
	public void render() {
		for (Renderable r : m_renderables) {
			r.render(getRenderer());
		}
	}
}
