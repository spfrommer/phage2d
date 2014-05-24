package engine.inputs.mouse;

import utils.physics.Vector;
import engine.inputs.InputEvent;

public abstract class MouseEvent extends InputEvent {
	private Vector m_coords;

	public MouseEvent(Vector coords) {
		setCoordinates(coords);
	}

	public Vector getCoordinates() {
		return m_coords;
	}

	public void setCoordinates(Vector coords) {
		m_coords = coords;
	}

	@Override
	public abstract MouseEvent clone();
}
