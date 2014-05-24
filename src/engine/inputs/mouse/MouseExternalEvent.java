package engine.inputs.mouse;

public class MouseExternalEvent extends MouseEvent {
	private MouseEvent m_event;

	public MouseExternalEvent(MouseEvent e) {
		super(e.getCoordinates());
		m_event = e;
	}

	public MouseEvent getEvent() {
		return m_event;
	}

	@Override
	public MouseEvent clone() {
		return new MouseExternalEvent(getEvent().clone());
	}
}
