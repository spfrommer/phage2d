package migrate.gui.test;

import java.awt.Rectangle;

import migrate.gui.widgets.Window;
import migrate.vector.Vector2f;
import engine.graphics.Renderer;

public class SimpleWindow extends Window {
	public static final int CONTENT_PANE_PADDING = 10;
	
	public static final int WINDOW_BAR_HEIGHT = 20;

	public static final int DRAG_BORDER_WIDTH = 10;
	public static final int TOP_BAR_DRAG_BORDER_WIDTH = 2;
	public static final int TOP_BAR_DRAG_CORNER_LENGTH = 5;
	
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
		//Left Bar
		mapping.add(new Rectangle(0, WINDOW_BAR_HEIGHT, 
								  DRAG_BORDER_WIDTH, getHeight() - WINDOW_BAR_HEIGHT - DRAG_BORDER_WIDTH),
						new Vector2f(-1f, 0f));
		//BottomLeft
		mapping.add(new Rectangle(0, getHeight() - DRAG_BORDER_WIDTH,
								  DRAG_BORDER_WIDTH, DRAG_BORDER_WIDTH), 
						new Vector2f(-1f, 1f));
		//Button Bar
		mapping.add(new Rectangle(DRAG_BORDER_WIDTH, getHeight() - DRAG_BORDER_WIDTH,
								  getWidth() - 2 * DRAG_BORDER_WIDTH, DRAG_BORDER_WIDTH),
					    new Vector2f(0f, 1f));
		//BottomRight
		mapping.add(new Rectangle(getWidth() - DRAG_BORDER_WIDTH, getHeight() - DRAG_BORDER_WIDTH, 
				  				  DRAG_BORDER_WIDTH, DRAG_BORDER_WIDTH),
				        new Vector2f(1f, 1f));
		//Right Bar
		mapping.add(new Rectangle(getWidth() - DRAG_BORDER_WIDTH, WINDOW_BAR_HEIGHT,
								  DRAG_BORDER_WIDTH, getHeight() - WINDOW_BAR_HEIGHT - DRAG_BORDER_WIDTH),
						new Vector2f(1f, 0f));
		/*Topleft corner
		mapping.add(new Rectangle(0, 0, TOP_BAR_DRAG_CORNER_LENGTH, TOP_BAR_DRAG_BORDER_WIDTH),
				new Vector2f(-1f, -1f));
		mapping.add(new Rectangle(0, 0, TOP_BAR_DRAG_BORDER_WIDTH, TOP_BAR_DRAG_CORNER_LENGTH),
				new Vector2f(-1f, -1f));*/
		return mapping;
	}

}
