package migrate.gui;

import java.util.ArrayList;

import migrate.input.Key;
import migrate.input.Keyboard;
import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;
import migrate.vector.Vector2f;

public abstract class ContainerWidget extends Widget {
	//private static final Logger logger = LoggerFactory.getLogger(ContainerWidget.class);
	
	private ArrayList<Widget> m_children = new ArrayList<Widget>();
	
	public void add(Widget w) {
		if (m_children.contains(w)) return;
		m_children.add(w);
	}
	public void remove(Widget w) { m_children.add(w); }

	public ArrayList<Widget> getChildren() { return m_children; }
	
	@Override
	public boolean hasFocusedChild() {
		if (super.hasFocusedChild()) return true;
		for (Widget w : m_children) if (w.hasFocusedChild()) return true;
		return false;
	}
	
	@Override
	public void validate() {
		for(Widget w : m_children) {
			w.validate();
		}
	}
	
	//----Input Methods------
	/*
	 * A container will redistribute input messages to their children based on whether
	 * the children are focused, or contain the supplied coordinates 
	 */
	@Override
	public void keyPressed(Keyboard keyboard, Key key) {
		super.keyPressed(keyboard, key);
		for (Widget w : m_children) {
			if (w.hasFocusedChild()) {
				w.keyPressed(keyboard, key);
				return;
			}
		}
	}
	@Override
	public void keyReleased(Keyboard keyboard, Key key) {
		super.keyReleased(keyboard, key);
		for (Widget w : m_children) {
			if (w.hasFocusedChild()) {
				w.keyReleased(keyboard, key);
				return;
			}
		}
	}

	@Override
	public void mouseMoved(Mouse m, int localX, int localY, Vector2f delta) {
		super.mouseMoved(m, localX, localY, delta);
		for (Widget w : m_children) {
			if (w.contains(localX, localY)) {
				w.mouseMoved(m, localX - w.getX(), localY - w.getY(), delta);
				if (!w.isMousedOver()) {
					w.mouseEntered(m, localX - w.getX(), localY - w.getY());
				}
			} else if (w.isMousedOver()) {
				w.mouseExited(m, localX - w.getX(), localY - w.getY());
			}
		}
	}
	@Override
	public void mouseWheelMoved(Mouse m, int localX, int localY, int dm) {
		super.mouseWheelMoved(m, localX, localY, dm);
		for (Widget w : m_children) {
			if (w.contains(localX, localY)) w.mouseWheelMoved(m, 
					localX - w.getX(), localY - w.getY(), dm);
		}
	}
	@Override
	public void mouseDelta(Mouse m, int localX, int localY, int deltaX, int deltaY) {
		super.mouseDelta(m, localX, localY, deltaX, deltaY);
		for (Widget w : m_children) {
			if (w.contains(localX, localY)) w.mouseDelta(m, localX - w.getX(), localY - w.getY(),
																deltaX, deltaY);
		}
	}
	@Override
	public void mouseButtonPressed(Mouse m, int localX, int localY, MouseButton button) {
		super.mouseButtonPressed(m, localX, localY, button);
		for (Widget w : m_children) {
			if (w.contains(localX, localY)) w.mouseButtonPressed(m, localX - w.getX(), localY - w.getY(), button);
		}
	}
	@Override
	public void mouseButtonReleased(Mouse m, int localX, int localY, MouseButton button) {
		super.mouseButtonReleased(m, localX, localY, button);
		for (Widget w : m_children) {
			if (w.contains(localX, localY)) w.mouseButtonReleased(m, localX - w.getX(), localY - w.getY(), button);
		}
	}
}
