package engine.gui.border;

import engine.graphics.Renderer;

public interface Border {
	public int getBottomBorder();
	public int getLeftBorder();
	public int getRightBorder();
	public int getTopBorder();
	
	public void render(Renderer r, int x, int y, int w, int h);
}
