package migrate.gui;

import java.util.ArrayList;

import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;
import migrate.vector.Vector2f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ContainerWidget extends Widget {
	//private static final Logger logger = LoggerFactory.getLogger(ContainerWidget.class);
	
	private ArrayList<Widget> m_children = new ArrayList<Widget>();
	
	public void add(Widget w) { m_children.add(w); }
	public void remove(Widget w) { m_children.add(w); }

	public ArrayList<Widget> getChildren() { return m_children; }
	
	//----Input Methods------
	/*
	 * A container will redistribute input messages to their children based on whether
	 * the children are focused, or contain the supplied coordinates 
	 */
	@Override
	public void mouseMoved(Mouse m, int localX, int localY, Vector2f delta) {
		for (Widget w : m_children) {
			if (w.contains(localX, localY)) {
				w.mouseMoved(m, localX - w.getX(), localY - w.getY(), delta);
				if (!w.isMousedOver()) {
					w.setMousedOver(true);
					w.mouseEntered(m, localX - w.getX(), localY - w.getY());
				}
			} else if (w.isMousedOver()) {
				w.setMousedOver(false);
				w.mouseExited(m, localX - w.getX(), localY - w.getY());
			}
		}
	}
	@Override
	public void mouseWheelMoved(Mouse m, int localX, int localY, int dm) {
		for (Widget w : m_children) {
			if (w.contains(localX, localY)) w.mouseWheelMoved(m, 
					localX - w.getX(), localY - w.getY(), dm);
		}
	}
	@Override
	public void mouseDelta(Mouse m, int localX, int localY, int deltaX, int deltaY) {
		for (Widget w : m_children) {
			if (w.contains(localX, localY)) w.mouseDelta(m, localX - w.getX(), localY - w.getY(),
																deltaX, deltaY);
		}
	}
	@Override
	public void mouseButtonPressed(Mouse m, int localX, int localY, MouseButton button) {
		for (Widget w : m_children) {
			if (w.contains(localX, localY)) w.mouseButtonPressed(m, localX - w.getX(), localY - w.getY(), button);
		}
	}
	@Override
	public void mouseButtonReleased(Mouse m, int localX, int localY, MouseButton button) {
		for (Widget w : m_children) {
			if (w.contains(localX, localY)) w.mouseButtonReleased(m, localX - w.getX(), localY - w.getY(), button);
		}
	}
}
