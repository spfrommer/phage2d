package engine.inputs.mouse;

import utils.physics.Vector;

public class MouseButtonEvent extends MouseEvent {
	private MouseButton m_button;
	private boolean m_state;

	public MouseButtonEvent(Vector coords, MouseButton button, boolean down) {
		super(coords);
		m_button = button;
		m_state = down;
	}

	public boolean isDown() {
		return m_state;
	}

	public MouseButton getButton() {
		return m_button;
	}

	@Override
	public MouseEvent clone() {
		return new MouseButtonEvent(getCoordinates().clone(), m_button, m_state);
	}
}
