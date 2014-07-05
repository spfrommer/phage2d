package migrate.gui.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import migrate.gui.Widget;
import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Button extends Widget {
	private HashSet<ActionListener> m_listeners = new HashSet<ActionListener>();
	
	private boolean m_down = false; 
	
	public boolean isDown() { return m_down; }
	
	public void addActionListener(ActionListener l) { m_listeners.add(l); }
	public void removeActionListener(ActionListener l) { m_listeners.remove(l); }
	
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
