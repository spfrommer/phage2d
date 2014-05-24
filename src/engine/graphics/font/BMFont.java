package engine.graphics.font;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import engine.graphics.Renderable;
import engine.graphics.Renderer;

public class BMFont implements Font {
	private int m_lineHeight;
	private int m_baseLineOffset;
	private HashMap<Character, Glyph> m_glyphs = new HashMap<Character, Glyph>();
	
	private String m_name;
	private int m_size;
	private boolean m_italic;
	private boolean m_bold;
	
	@Override
	public int getAscending() { return m_baseLineOffset; }
	@Override
	public int getDescending() { return m_lineHeight - m_baseLineOffset; }
	@Override
	public int getLineHeight() { return m_lineHeight; }
	
	@Override
	public String getName() { return m_name; }
	@Override
	public int getSize() { return m_size; }
	
	@Override
	public boolean isItalic() { return m_italic; }
	@Override
	public boolean isBold() { return m_bold; }
	
	public void setAscending(int ascending) { m_baseLineOffset = ascending; }
	public void setLineHeight(int height) { m_lineHeight = height; }
	public void setName(String name) { m_name = name; }
	public void setSize(int size) { m_size = size; }
	public void setItalic(boolean i) { m_italic = i; }
	public void setBold(boolean b) { m_bold = b; } 
	
	public void addGlyph(char c, Glyph g) {
		m_glyphs.put(c, g);
	}
	
	public Glyph getGlyph(char c) {
		return m_glyphs.get(c);
	}
	/**
	 * Retrieves the minimum size of a string of text
	 */
	@Override
	public Rectangle getSize(String text) {
		int height = 0;
		int width = 0;
		//Initialize y to the maximum int
		int y = Integer.MAX_VALUE;
		
		char[] chars = text.toCharArray();
		//First, find the lowest yoffset
		for (char c : chars) {
			Glyph g = getGlyph(c);
			y = Math.min(g.getYOff(), y);
		}
		for (char c : chars) {
			Glyph g = getGlyph(c);
			height = Math.max(g.getHeight() + (g.getYOff() - y), height);
			width += g.getXAdvance();
		}
		return new Rectangle(0, y, width, height);
	}
	
	public static class BMGlyph implements Renderable, Glyph {
		private final char m_char;
		private final BufferedImage m_glyph;
		
		//Describes the y offset from the top line of the font
		//+ is below, - is above so the y is 
		//- getAscending() (to move the cursor to the top of the line) + yoffset
		private final int m_xoffset;
		private final int m_yoffset;
		private final int m_xadvance;
		
		private final Font m_font;
		
		public BMGlyph(Font f, char c, BufferedImage image, int xoff, int yoff, int xadv) {
			m_font = f;
			m_char = c;
			m_glyph = image;
			m_xoffset = xoff;
			m_yoffset = yoff;
			m_xadvance = xadv;
		}
		public Font getFont() { return m_font; }
		public char getChar() { return m_char; }
		public BufferedImage getImage() { return m_glyph; }
		
		public int getXOff() { return m_xoffset; }
		public int getYOff() { return m_yoffset; }
		public int getXAdvance() { return m_xadvance; }
		
		public int getWidth() { return m_glyph.getWidth(); }
		public int getHeight() { return m_glyph.getHeight(); }
		
		public void render(Renderer r) {
			//Translate across the xoffset and down by the yoffset
			r.drawImage(m_glyph, m_xoffset, m_yoffset - getFont().getAscending());
		}
		public void renderAndTranslate(Renderer r) {
			render(r);
			r.translate(m_xadvance, 0);
		}
	}
}
