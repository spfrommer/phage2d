package migrate.gui.test;

import migrate.gui.widgets.Button;
import engine.graphics.Renderer;

public class SimpleButton extends Button {
	@Override
	public void renderWidget(Renderer r) {
		r.drawRect(0, 0, getWidth(), getHeight());
	}
}
