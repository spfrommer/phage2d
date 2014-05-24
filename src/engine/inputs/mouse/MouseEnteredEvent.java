package engine.inputs.mouse;

import utils.physics.Vector;

public class MouseEnteredEvent extends MouseEvent {
	public MouseEnteredEvent(Vector pos) {
		super(pos);
	}

	@Override
	public MouseEvent clone() {
		return new MouseEnteredEvent(getCoordinates().clone());
	}
}
