package migrate.gui.test;

import java.awt.Rectangle;

import migrate.gui.Dimension;
import migrate.gui.widgets.TextField;
import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;
import engine.graphics.Color;
import engine.graphics.Renderer;
import engine.graphics.font.Font;
import engine.graphics.font.Font.Glyph;

public class SimpleTextField extends TextField {
	private static final Font FONT = SimpleThemeConstants.LABEL_FONT;
	private static final Color TEXT_COLOR = SimpleThemeConstants.COLOR_SOLID;
	private static final Color BORDER_COLOR = SimpleThemeConstants.COLOR_SOLID;
	
	//A small inset to prevent the border from going out of the widget
	private static final int BORDER_INSET = 2;
	
	private static final int CURSOR_ROUGH_SIZE = 3;
	//The amount of padding to put between the top and side of the text and the border
	private static final int TEXT_PADDING = 5;
	private static final int TEXT_CLIP_PADDING = 3;
	private static final int TOTAL_TEXT_PADDING = BORDER_INSET + TEXT_PADDING;
	private static final int TOTAL_TEXT_CLIP_PADDING = BORDER_INSET + TEXT_CLIP_PADDING;
	
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
	private int getTextPos(int mouseX) {
		int clickX = mouseX - TOTAL_TEXT_PADDING;
		char[] characters = getTextBuilder().toString().toCharArray();
		int xPosCounter = 0;
		for (int index = 0; index < characters.length; index++) {
			Glyph g = FONT.getGlyph(characters[index]);
			xPosCounter += g.getWidth(); 
			if (xPosCounter > clickX)  return index;
		}
		return characters.length;
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
		
		//Set the clip
		r.setClip(TOTAL_TEXT_CLIP_PADDING, TOTAL_TEXT_CLIP_PADDING,
				  getWidth() - 2 * TOTAL_TEXT_CLIP_PADDING, FIXED_HEIGHT - TOTAL_TEXT_CLIP_PADDING);

		//Check if we need to scroll
		int textMaxWidth = getWidth() - (2 * TOTAL_TEXT_CLIP_PADDING);
		if (cursorPos > textMaxWidth)  {
			int scrollAmount = textMaxWidth - (cursorPos + CURSOR_ROUGH_SIZE);
			r.translate(scrollAmount, 0);
		}		
		r.drawString(getText(), TOTAL_TEXT_PADDING, FONT.getAscent() + BORDER_INSET + TEXT_PADDING);
		
		//Check if cursor should be displayed
		if (isFocused() && ((System.currentTimeMillis() % CURSOR_BLINK_MILLIS) > CURSOR_DISAPPEAR_MILLIS)) {
			//Draw a vertical line
			r.drawLine(TOTAL_TEXT_PADDING + cursorPos, TOTAL_TEXT_PADDING, 
					TOTAL_TEXT_PADDING + cursorPos, TOTAL_TEXT_PADDING + FONT.getHeight());
		}
		r.setFont(null);
	}
	@Override
	public void mouseButtonPressed(Mouse m, int localX, int localY, MouseButton button) {
		super.mouseButtonPressed(m, localX, localY, button);
		int index = getTextPos(localX);
		setCursorPosition(index);
	}
}
