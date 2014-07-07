package migrate.gui.widgets;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import migrate.gui.Widget;
import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;
import migrate.input.MouseListener;
import migrate.vector.Vector2f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import engine.graphics.Renderer;

public abstract class Button extends Widget {
	private static final Logger logger = LoggerFactory.getLogger(Button.class);
	
	private HashSet<ActionListener> m_listeners = new HashSet<ActionListener>();
	
	private Widget m_content = null;
	
	private boolean m_down = false; 
	
	public void setContent(Widget widget) { m_content = widget; }
	public void setDown(boolean down) { m_down = down; }
	public Widget getContent() { return m_content; }
	public boolean isDown() { return m_down; }


	public void addActionListener(ActionListener l) { m_listeners.add(l); }
	public void removeActionListener(ActionListener l) { m_listeners.remove(l); }
	
	@Override
	public void validate() {
		if (m_content == null) return;
		Rectangle childBounds = getChildWidgetBounds();
		m_content.setBounds(childBounds);
	}
	
	@Override
	public void renderWidget(Renderer r) {
		renderBackground(r);
		//Now render the child widget
		if (m_content != null) m_content.render(r);
	}
	
	public abstract void renderBackground(Renderer r);
	public abstract Rectangle getChildWidgetBounds();
	
	//------- Input methods-------
	@Override
	public void mouseButtonPressed(Mouse m, int localX, int localY, MouseButton button) {
		//First pass click to child
		if (m_content != null && m_content.contains(localX, localY)) 
			m_content.mouseButtonPressed(m, localX - m_content.getX(), localY - m_content.getY(), button);

		//Now check for a button being pressed
		if (!button.getName().equals(MouseButton.LEFT_BUTTON_NAME)) return;
		setDown(true);
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
	public void mouseMoved(Mouse m, int localX, int localY, Vector2f delta) {
		if (m_content != null && m_content.contains(localX, localY)) 
			m_content.mouseMoved(m, localX - m_content.getX(), localY - m_content.getY(), delta);
	}
	@Override
	public void mouseEntered(Mouse m, int localX, int localY) {
		if (m_content != null && m_content.contains(localX, localY)) 
			m_content.mouseEntered(m, localX - m_content.getX(), localY - m_content.getY());
	}
	@Override
	public void mouseExited(Mouse m, int localX, int localY) {
		if (m_content != null && m_content.contains(localX, localY)) 
			m_content.mouseExited(m, localX - m_content.getX(), localY - m_content.getY());
	}


	@Override
	public void mouseButtonReleased(Mouse m, int localX, int localY, MouseButton button) {
		if (m_content != null && m_content.contains(localX, localY)) 
			m_content.mouseButtonReleased(m, localX - m_content.getX(), localY - m_content.getY(), button);
	}
}
