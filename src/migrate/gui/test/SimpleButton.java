package migrate.gui.test;

import java.awt.Rectangle;

import migrate.gui.Dimension;
import migrate.gui.Widget;
import migrate.gui.widgets.Button;
import engine.graphics.Renderer;

public class SimpleButton extends Button {
	public static final int BORDER_PADDING = 5;
	
	@Override
	public Dimension getMinSize() {
		Widget content = getContent();
		Dimension cs = null;
		if (content != null) cs = content.getMinSize();
		if (cs == null) cs = new Dimension(); 
		return new Dimension(2 * BORDER_PADDING + cs.getWidth(), 2 * BORDER_PADDING + cs.getHeight());
	}
	@Override
	public Dimension getPreferredSize() {
		Widget content = getContent();
		Dimension cs = null;
		if (content != null) cs = content.getPreferredSize();
		if (cs == null) cs = new Dimension(); 
		return new Dimension(2 * BORDER_PADDING + cs.getWidth(), 2 * BORDER_PADDING + cs.getHeight());
	}
	@Override
	public Rectangle getChildWidgetBounds() {
		return new Rectangle(BORDER_PADDING, BORDER_PADDING, 
							  		getWidth() - 2 * BORDER_PADDING, 
									getHeight() - 2 * BORDER_PADDING);
	}
	@Override
	public void renderBackground(Renderer r) {
		if (isDown()) {
			r.setColor(SimpleThemeConstants.COLOR_SOLID);
			r.fillRect(0, 0, getWidth(), getHeight());
		} else {
			r.setColor(SimpleThemeConstants.COLOR_TRANSLUCENT);
			r.fillRect(0, 0, getWidth(), getHeight());
			r.setColor(SimpleThemeConstants.COLOR_SOLID);
			r.drawRect(1, 1, getWidth() - 1, getHeight() - 1);
		}
	}

}
