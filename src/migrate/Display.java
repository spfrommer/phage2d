package migrate;

import migrate.input.Keyboard;
import migrate.input.Mouse;
import engine.core.implementation.camera.base.ResizeListener;
import engine.graphics.Renderer;

public interface Display {
	public int getWidth();
	public int getHeight();
	
	public void addResizedListener(ResizeListener rl);
	
	public Mouse getMouse();
	public Keyboard getKeyboard();
	public Renderer getRenderer();
	
	public void init();
	public void destroy();
	public boolean closeRequested();
	public void update(int fps);
}
