package migrate.gui.test;

import engine.graphics.Color;
import engine.graphics.font.Font;
import engine.graphics.font.JavaFontConverter;

public class SimpleThemeConstants {
	public static Color COLOR_TRANSLUCENT = new Color(0.35f, 0.51f, 0.90f, 0.4f);
	public static Color COLOR_SOLID = new Color(0.35f, 0.51f, 0.95f, 1f);
	public static Font LABEL_FONT = null;
	public static Font FRAME_TITLE_FONT = null;
	
	static {
		LABEL_FONT = JavaFontConverter.s_convert(new java.awt.Font("Serif", java.awt.Font.PLAIN, 17));
		FRAME_TITLE_FONT = JavaFontConverter.s_convert(new java.awt.Font("Serif", java.awt.Font.PLAIN, 15));
	}
}
