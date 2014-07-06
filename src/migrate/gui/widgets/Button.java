package migrate.gui.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import migrate.gui.Widget;
import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;
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
	}
	@Override
	public void mouseButtonReleased(Mouse m, int localX, int localY, MouseButton button) {
		if (!button.getName().equals(MouseButton.LEFT_BUTTON_NAME)) return;
		if (!m_down) return;
		m_down = false;
		for (ActionListener l : m_listeners) {
			l.actionPerformed(new ActionEvent(this, button.getId(), ""));
		}
	}
}
