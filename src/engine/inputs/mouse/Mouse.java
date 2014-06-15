package engine.inputs.mouse;

import java.util.ArrayList;

import utils.physics.Vector;

public class Mouse {
	protected int m_x = 0;
	protected int m_y = 0;

	protected ArrayList<MouseListener> m_clickedListeners = new ArrayList<MouseListener>();
	protected ArrayList<MouseButton> m_buttons = new ArrayList<MouseButton>();

	protected Mouse() {
	}

	protected void mouseMoved(int x, int y) {
		if (x == m_x && y == m_y)
			return;
		int deltaX = x - m_x;
		int deltaY = y - m_y;
		m_x = x;
		m_y = y;
		synchronized (m_clickedListeners) {
			for (MouseListener mcl : m_clickedListeners) {
				mcl.mouseEvent(new MouseMovedEvent(getCoordinates(), new Vector(deltaX, deltaY)));
			}
		}
	}

	public void setButtonState(MouseButton button, boolean state) {
		if (button.isDown() == state)
			return;// No change

		MouseButtonEvent event = new MouseButtonEvent(new Vector(m_x, m_y), button, state);

		button.mouseEvent(event);

		synchronized (m_clickedListeners) {
			for (MouseListener mcl : m_clickedListeners) {
				mcl.mouseEvent(event);
			}
		}

		synchronized (m_clickedListeners) {
			if (state == false && button.getName().equals(MouseButton.LEFT_NAME)) {// Mouse was released notify click
				for (MouseListener mcl : m_clickedListeners)
					mcl.mouseEvent(new MouseClickedEvent(getCoordinates()));
			}
		}
	}

	public void addMouseListener(MouseListener mcl) {
		synchronized (m_clickedListeners) {
			m_clickedListeners.add(mcl);
		}
	}

	public void addMouseButton(MouseButton b) {
		m_buttons.add(b);
	}

	public MouseButton getMouseButton(String name) {
		for (MouseButton b : m_buttons) {
			if (b.getName().equals(name))
				return b;
		}
		return null;
	}

	// TODO: switch to hashmap?
	public MouseButton getMouseButton(int id) {
		for (MouseButton b : m_buttons) {
			if (b.getID() == id)
				return b;
		}
		return null;
	}

	@Deprecated
	public boolean isLeftButtonDown() {
		return getMouseButton("Left").isDown();
	}

	@Deprecated
	public boolean isMiddleButtonDown() {
		return getMouseButton("Middle").isDown();
	}

	@Deprecated
	public boolean isRightButtonDown() {
		return getMouseButton("Right").isDown();
	}

	public Vector getCoordinates() {
		return (new Vector(m_x, m_y));
	}
}
