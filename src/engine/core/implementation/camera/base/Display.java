package engine.core.implementation.camera.base;

import engine.graphics.Renderable;
import engine.graphics.Renderer;

public interface Display {
	public int getWidth();

	public int getHeight();

	public void addResizedListener(ResizeListener rl);

	/**
	 * This methods adds a renderable that will be rendered when render() is called Order is preserved
	 * 
	 * @param r
	 *            the renderable to add
	 */
	public void addRenderable(Renderable r);

	public void removeRenderable(Renderable r);

	public void init();

	public void destroy();

	public boolean destroyRequested();

	public Renderer getRenderer();

	public void update();

	public void update(int fps);

	/**
	 * Renders all the renderables to screen
	 */
	public void render();
}
