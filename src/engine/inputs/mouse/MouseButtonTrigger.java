package engine.inputs.mouse;

import engine.inputs.InputTrigger;

public class MouseButtonTrigger extends InputTrigger implements MouseListener {
	public MouseButtonTrigger() {}
	public MouseButtonTrigger(MouseButton button) {
		button.addListener(this);
	}
	
	public void mouseEvent(MouseEvent event) {
		if (event instanceof MouseButtonEvent) {
			boolean down = ((MouseButtonEvent) event).isDown();
			if (down) trigger(1f);
			else trigger(0f);
		}
	}
}
