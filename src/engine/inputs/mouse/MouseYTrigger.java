package engine.inputs.mouse;

import engine.inputs.InputTrigger;

public class MouseYTrigger extends InputTrigger implements MouseListener {
	public MouseYTrigger() {}
	public MouseYTrigger(Mouse mouse) {
		mouse.addMouseListener(this);
	}
	
	public void mouseEvent(MouseEvent e) {
		if (e instanceof MouseMovedEvent) {
			if (((MouseMovedEvent) e).getDelta().getY() != 0) trigger((float) e.getCoordinates().getY());
		}
	}
}
