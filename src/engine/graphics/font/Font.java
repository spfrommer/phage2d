package engine.graphics.font;

import java.awt.Rectangle;

import engine.graphics.Renderer;

public interface Font {
	
	public int getAscending();
	public int getDescending();
	public int getLineHeight();
	
	public String getName();
	public int getSize();
	
	public boolean isItalic();
	public boolean isBold();
	
	public Glyph getGlyph(char c);
	
	public Rectangle getSize(String text);
	
	public interface Glyph {
		public char getChar();

		//MAYBE remove these? Maybe later
		
		/**
		 * Returns the xOffset at the beginning of the character
		 * @return the xOffset
		 */
		public int getXOff();
		/**
		 * Returns the yoffset where the yoffset is
		 * + if offset is below
		 * - if offset is above
		 * this is equivalent to the descending length of the char
		 * to render this, just add it to the y location
		 * @return
		 */
		public int getYOff();
		
		/**
		 * How many pixels to advance the cursor after
		 * drawing the char
		 * @return the xAdvance
		 */
		public int getXAdvance();
		
		public int getHeight();
		public int getWidth();
		
		/**
		 * Renders the char but does not move the cursor
		 * @param r the Renderer to use
		 */
		public void render(Renderer r);
		/**
		 * Renders the char and moves the cursor
		 * Equivalent to:
		 * <code>
		 * render(r);
		 * r.translate(getXAdvance(), 0);
		 * </code>
		 * 
		 * @param r the Renderer to use
		 */
		public void renderAndTranslate(Renderer r);
	}
}
