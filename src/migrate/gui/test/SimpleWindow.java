package migrate.gui.test;

import java.awt.Rectangle;

import engine.graphics.Renderer;
import migrate.gui.widgets.Window;

public class SimpleWindow extends Window {
	public static final int WINDOW_BAR_HEIGHT = 20;
	public static final int CONTENT_PANE_PADDING = 2;
	
	@Override
	public void renderFrame(Renderer r) {
		//Draw outer frame
		r.drawRect(0, 0, getWidth(), getHeight());
		//Draw top window bar
		r.drawRect(0, 0, getWidth(), WINDOW_BAR_HEIGHT);
	}
	@Override
	protected Rectangle getContentPaneBounds() {
		return new Rectangle(CONTENT_PANE_PADDING, WINDOW_BAR_HEIGHT + CONTENT_PANE_PADDING,
							 	getWidth() - 2 * CONTENT_PANE_PADDING, 
							 	getHeight() - WINDOW_BAR_HEIGHT - 2 * CONTENT_PANE_PADDING);
	}
	@Override
	protected Rectangle getWindowBarBounds() {
		return new Rectangle(0, 0, getWidth(), WINDOW_BAR_HEIGHT);
	}

}
