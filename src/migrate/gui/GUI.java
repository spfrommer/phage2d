package migrate.gui;

import java.util.ArrayList;

import migrate.input.Key;
import migrate.input.KeyListener;
import migrate.input.Keyboard;
import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;
import migrate.input.MouseListener;
import migrate.vector.Vector2f;
import engine.graphics.Renderable;
import engine.graphics.Renderer;

public class GUI implements Renderable, MouseListener, KeyListener {
	private ArrayList<Widget> m_widgets = new ArrayList<Widget>();
	
	public void add(Widget w) { m_widgets.add(w); }
	public void remove(Widget w) { m_widgets.add(w); }

	public ArrayList<Widget> getWidgets() { return m_widgets; }
	
	@Override
	public void render(Renderer r) {
		for (Widget w : m_widgets) {
			w.render(r);
		}
	}

	//TODO: Implement key message sending
	@Override
	public void keyPressed(Keyboard k, Key key) {}
	@Override
	public void keyReleased(Keyboard k, Key key) {}
	
	@Override
	public void mouseMoved(Mouse m, int x, int y, Vector2f delta) {
		for (Widget w : m_widgets) {
			if (w.contains(x, y)) w.mouseMoved(m, x, y, delta);
		}
	}
	@Override
	public void mouseWheelMoved(Mouse m, int dm) {
		for (Widget w : m_widgets) {
			if (w.contains(m.getX(), m.getY())) w.mouseWheelMoved(m, m.getX(), m.getY(), dm);
		}
	}
	@Override
	public void mouseDelta(Mouse m, int dx, int dy) {
		for (Widget w : m_widgets) {
			if (w.contains(m.getX(), m.getY())) w.mouseDelta(m, m.getX(), m.getY(),dx, dy);
		}
	}
	@Override
	public void mouseButtonPressed(Mouse m, MouseButton button) {
		for (Widget w : m_widgets) {
			if (w.contains(m.getX(), m.getY())) w.mouseButtonPressed(m, m.getX(), m.getY(), button);
		}
	}
	@Override
	public void mouseButtonReleased(Mouse m, MouseButton button) {
		for (Widget w : m_widgets) {
			if (w.contains(m.getX(), m.getY())) w.mouseButtonReleased(m, m.getX(), m.getY(), button);
		}
	}
}
