package engine.gui.theme;

import java.awt.Color;

import engine.graphics.Renderer;
import engine.gui.AnimationState;

public interface Image {
	public int getWidth();
	public int getHeight();
	
	public int getMinWidth();
	public int getMinHeight();
	
	public void tint(Color color);
	
	public void render(Renderer r, AnimationState as, int x, int y);
	public void render(Renderer r, AnimationState as, int x, int y, int width, int height);
}
