package engine.gui;

import engine.graphics.Renderer;
import engine.inputs.InputEvent;

public class GUI extends Widget {
	
	@Override
	public void renderWidget(Renderer renderer) {
		for (Widget w : getChildren()) {
			w.render(renderer);
		}
	}

}
