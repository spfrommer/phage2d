package engine.inputs.mouse;

import utils.physics.Vector;

public class MouseExitedEvent extends MouseEvent {
	public MouseExitedEvent(Vector coords) {
		super(coords);
	}

	@Override
	public MouseEvent clone() {
		return new MouseExitedEvent(getCoordinates().clone());
	}
}
