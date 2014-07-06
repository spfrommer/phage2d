package migrate.gui.test;

import java.awt.Rectangle;

import migrate.gui.widgets.Window;
import migrate.vector.Vector2f;
import engine.graphics.Renderer;

public class SimpleWindow extends Window {
	public static final int WINDOW_BAR_HEIGHT = 20;
	public static final int CONTENT_PANE_PADDING = 2;
	public static final int DRAG_CORNER_SIDE_LENGTH = 10;
	
	@Override
	public void renderFrame(Renderer r) {
		r.setColor(SimpleThemeConstants.COLOR_SOLID);
		//Draw outer frame
		r.drawRect(0, 0, getWidth(), getHeight());
		//Draw top window bar
		r.drawRect(0, 0, getWidth(), WINDOW_BAR_HEIGHT);
		
		r.setColor(SimpleThemeConstants.COLOR_TRANSLUCENT);
		r.fillRect(0, 0, getWidth(), getHeight());
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
	@Override
	protected ResizeAreaMapping getResizeAreaMapping() {
		ResizeAreaMapping mapping = new ResizeAreaMapping();
		mapping.add(new Rectangle(getWidth() - DRAG_CORNER_SIDE_LENGTH, getHeight() - DRAG_CORNER_SIDE_LENGTH, 
									DRAG_CORNER_SIDE_LENGTH, DRAG_CORNER_SIDE_LENGTH), new Vector2f(1f, 1f));
		return mapping;
	}

}
