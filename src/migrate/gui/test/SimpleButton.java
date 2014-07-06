package migrate.gui.test;

import migrate.gui.Dimension;
import migrate.gui.widgets.Button;
import engine.graphics.Renderer;

public class SimpleButton extends Button {

	@Override
	public void renderWidget(Renderer r) {
		
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
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(10, 10);
	}
}
