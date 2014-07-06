package migrate.gui.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import migrate.gui.Widget;
import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;
import migrate.input.MouseListener;
import migrate.vector.Vector2f;

public abstract class Button extends Widget {
	//private static final Logger logger = LoggerFactory.getLogger(Button.class);
	
	private HashSet<ActionListener> m_listeners = new HashSet<ActionListener>();
	
	private boolean m_down = false; 
	
	public boolean isDown() { return m_down; }
	
	public void addActionListener(ActionListener l) { m_listeners.add(l); }
	public void removeActionListener(ActionListener l) { m_listeners.remove(l); }
	
	@Override
	public void mouseMoved(Mouse m, int localX, int localY, Vector2f delta) {
		//logger.debug("Mouse Moved in Button: " + localX + " " + localY);
	}
	@Override
	public void mouseEntered(Mouse m, int localX, int localY) {
		//logger.debug("Mouse Entered (" + localX + ", " + localY + ")");
	}
	@Override
	public void mouseExited(Mouse m, int localX, int localY) {
		//logger.debug("Mouse Exited (" + localX + ", " + localY + ")");
	}
	
	@Override
	public void mouseButtonPressed(Mouse m, int localX, int localY, MouseButton button) {
		if (!button.getName().equals(MouseButton.LEFT_BUTTON_NAME)) return;
		m_down = true;
		//Add a listener to the mouse to listen for any releases, even outside of the widget
		m.addListener(new MouseListener() {
			public void mouseMoved(Mouse m, int x, int y, Vector2f delta) {}
			public void mouseWheelMoved(Mouse m, int dm) {}
			public void mouseDelta(Mouse m, int dx, int dy) {}
			public void mouseButtonPressed(Mouse m, MouseButton button) {}
			public void mouseButtonReleased(Mouse m, MouseButton button) {
				if (!button.getName().equals(MouseButton.LEFT_BUTTON_NAME)) return;
				if (!m_down) return;
				m_down = false;
				for (ActionListener l : m_listeners) {
					l.actionPerformed(new ActionEvent(this, button.getId(), ""));
				}
				m.removeListener(this);
			}
		});
	}
	@Override
	public void mouseButtonReleased(Mouse m, int localX, int localY, MouseButton button) {}
}
