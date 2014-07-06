package migrate.gui.widgets;

import java.awt.Rectangle;

import migrate.gui.ContainerWidget;
import migrate.gui.Widget;

import engine.graphics.Renderer;

public class Panel extends ContainerWidget {
	@Override
	public void renderWidget(Renderer r) {
		r.setClip(new Rectangle(0, 0, getWidth(), getHeight()));
		//r.fillRect(0, 0, getWidth(), getHeight());
		for (Widget w : getChildren()) {
			w.render(r);
		}
		r.setClip(null);
	}
}
