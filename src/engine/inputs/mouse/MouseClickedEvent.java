package engine.inputs.mouse;

import utils.physics.Vector;

public class MouseClickedEvent extends MouseEvent {
	public MouseClickedEvent(Vector coords) {
		super(coords);
	}

	@Override
	public MouseEvent clone() {
		return new MouseClickedEvent(getCoordinates().clone());
	}
}
