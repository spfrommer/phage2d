package migrate.gui.test;

import java.awt.Rectangle;

import migrate.gui.Dimension;
import migrate.gui.widgets.TextField;
import engine.graphics.Color;
import engine.graphics.Renderer;
import engine.graphics.font.Font;

public class SimpleTextField extends TextField {
	private static final Font FONT = SimpleThemeConstants.LABEL_FONT;
	private static final Color TEXT_COLOR = SimpleThemeConstants.COLOR_SOLID;
	private static final Color BORDER_COLOR = SimpleThemeConstants.COLOR_SOLID;
	
	//A small inset to prevent the border from going out of the widget
	private static final int BORDER_INSET = 2;
	//The amount of padding to put between the top and side of the text and the border
	private static final int TEXT_PADDING = 2;
	private static final int TOTAL_TEXT_PADDING = BORDER_INSET + TEXT_PADDING;
	
	private static final int FIXED_HEIGHT = FONT.getHeight() + 2 * BORDER_INSET + 2 * TEXT_PADDING;
	private static final int MINIMUM_WIDTH = 20;
	
	//How often to blink
	private static final int CURSOR_BLINK_MILLIS = 1000;
	//How long to blink
	private static final int CURSOR_DISAPPEAR_MILLIS = 250;

	public SimpleTextField() {
		setMinSize(new Dimension(MINIMUM_WIDTH, FIXED_HEIGHT));
		setMaxSize(new Dimension(Integer.MAX_VALUE, FIXED_HEIGHT));
	}

	@Override
	public void renderWidget(Renderer r) {
		r.setColor(BORDER_COLOR);
		r.drawRect(BORDER_INSET, BORDER_INSET, getWidth() - BORDER_INSET, getHeight() - BORDER_INSET);
		r.setFont(FONT);
		r.setColor(TEXT_COLOR);
		
		String beforeCursor = getPrecursorText();
		Rectangle bounds = FONT.getBounds(beforeCursor);
		//cursor Position in pixels
		int cursorPos = (int) bounds.getWidth();
		
		int textMaxWidth = getWidth() - (2 * BORDER_INSET + 2 * TEXT_PADDING);
		
		//Set the clip
		r.setClip(TOTAL_TEXT_PADDING, TOTAL_TEXT_PADDING,
				  textMaxWidth, FIXED_HEIGHT - TOTAL_TEXT_PADDING);

		//Check if we need to scroll
		if (cursorPos > textMaxWidth) {
			int scrollAmount = textMaxWidth - cursorPos;
			r.translate(scrollAmount, 0);
		}
								
		r.drawString(getText(), TOTAL_TEXT_PADDING, FONT.getAscent() + BORDER_INSET + TEXT_PADDING);
		
		//Check if cursor should be displayed
		if ((System.currentTimeMillis() % CURSOR_BLINK_MILLIS) > CURSOR_DISAPPEAR_MILLIS) {
			//Draw a vertical line
			r.drawLine(TOTAL_TEXT_PADDING + cursorPos, TOTAL_TEXT_PADDING, 
					   TOTAL_TEXT_PADDING + cursorPos, TOTAL_TEXT_PADDING + FONT.getHeight());
		}
		r.setFont(null);
	}
}
