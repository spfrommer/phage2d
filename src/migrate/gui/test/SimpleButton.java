package migrate.gui.test;

import migrate.gui.widgets.Button;
import engine.graphics.Color;
import engine.graphics.Renderer;

public class SimpleButton extends Button {
	public static Color BACKGROUND_NORMAL = new Color(1f, 1f, 1f, 0.4f);
	@Override
	public void renderWidget(Renderer r) {
		
		if (isDown()) r.fillRect(0, 0, getWidth(), getHeight());
		else {
			r.setColor(BACKGROUND_NORMAL);
			r.fillRect(0, 0, getWidth(), getHeight());
			r.setColor(Color.WHITE);
			r.drawRect(0, 0, getWidth(), getHeight());
		}
	}
}
