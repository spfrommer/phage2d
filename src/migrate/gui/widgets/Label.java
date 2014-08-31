package migrate.gui.widgets;

import migrate.gui.Widget;
import engine.graphics.Color;
import engine.graphics.font.Font;

public abstract class Label extends Widget {
	private String m_text;
	private Color m_color;
	private Font m_font;
	
	public Label() {}
	public Label(String text) {
		m_text = text;
	}
	
	public void setText(String text) { m_text = text; }
	public void setColor(Color color) { m_color = color; }
	public void setFont(Font font) { m_font = font; }
	public String getText() { return m_text; }
	public Color getColor() { return m_color; }
	public Font getFont() { return m_font; }
}
