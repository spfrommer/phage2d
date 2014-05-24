package engine.inputs.mouse;

import utils.physics.Vector;

public class MouseMovedEvent extends MouseEvent {
	private Vector m_delta;

	public MouseMovedEvent(Vector coordinates, Vector delta) {
		super(coordinates);
		m_delta = delta;
	}



	public Vector getDelta() {
		return m_delta;
	}

	@Override
	public MouseEvent clone() {
		return new MouseMovedEvent(getCoordinates().clone(), getDelta().clone());
	}
}
