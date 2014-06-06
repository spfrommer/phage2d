package engine.graphics.font;

import java.awt.Font;

public class JavaFontConverter {
	public static BMFont convert(Font font, char[] chars) {
		BMFont bmfont = new BMFont();
		bmfont.setSize(font.getSize());
		bmfont.setItalic(font.isItalic());
		bmfont.setBold(font.isBold());
		bmfont.setName(font.getFontName());
		return bmfont;
	}
}
