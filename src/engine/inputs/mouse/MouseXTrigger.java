package engine.inputs.mouse;

import engine.inputs.InputTrigger;

public class MouseXTrigger extends InputTrigger implements MouseListener {
	public MouseXTrigger() {}
	public MouseXTrigger(Mouse mouse) {
		mouse.addMouseListener(this);
	}
	
	public void mouseEvent(MouseEvent e) {
		if (e instanceof MouseMovedEvent) {
			if(((MouseMovedEvent) e).getDelta().getX() != 0) trigger((float) e.getCoordinates().getX());
		}
	}
}