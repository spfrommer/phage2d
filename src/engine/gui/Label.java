package engine.gui;

import java.awt.geom.Rectangle2D;

import engine.graphics.Color;
import engine.graphics.Renderer;
import engine.graphics.font.Font;
import engine.gui.theme.Theme;

public class Label extends ThemedWidget {
	public String m_text = "";
	public Font   m_font;
	public Label() {}
	public Label(String text, Font f) {
		setText(text);
		setFont(f);
	}
	public void setFont(Font font) { m_font = font; }
	public void setText(String text) { m_text = text; }
	public String getText() { return m_text; }
	public Font  getFont() { return m_font; }
	
	@Override
	public void validate() {
		super.validate();
		setSize(getPreferredSize());
	}
	
	@Override
	public void renderWidget(Renderer renderer) {
		Color old = renderer.getColor();
		Font oldFont = renderer.getFont();
		
		Font font = m_font;
		if (font == null) {
			Theme t = getWidgetTheme();
			if (t != null) {
				///
			}
		} else {
			
		}
		
		renderer.setColor(Color.BLACK);
		renderer.setFont(getFont());
		Dimension size = getPreferredSize();
		renderer.drawString(getText(), getX(), getY() + (int) size.getHeight());

		renderer.setFont(oldFont);
		renderer.setColor(old);
	}
	
	@Override
	public Dimension getPreferredSize() {
		Rectangle2D bounds = m_font.getBounds(getText());
		return new Dimension((int) bounds.getWidth(), (int) bounds.getHeight());
	}
	@Override
	public String getThemeName() {
		return "label";
	}
}
