package migrate.gui;

import migrate.input.Key;
import migrate.input.Keyboard;
import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;
import migrate.vector.Vector2f;

public abstract class FocusPolicy {
	public void keyPressed(Widget w, Keyboard keyboard, Key key) {}
	public void keyReleased(Widget w, Keyboard keyboard, Key key) {}
	public void mouseMoved(Widget w, Mouse m, int localX, int localY, Vector2f delta) {}
	public void mouseWheelMoved(Widget w, Mouse m, int localX, int localY, int dm) {}
	public void mouseDelta(Widget w, Mouse m, int localX, int localY, int deltaX, int deltaY) {}
	public void mouseButtonPressed(Widget w, Mouse m, int localX, int localY, MouseButton button) {}
	public void mouseButtonReleased(Widget w, Mouse m, int localX, int localY, MouseButton button) {}
	public void mouseEntered(Widget w, Mouse m, int localX, int localY) {}
	public void mouseExited(Widget w, Mouse m, int localX, int localY) {}
	
	public static class ClickFocusPolicy extends FocusPolicy {
		@Override
		public void mouseButtonPressed(Widget w, Mouse m, int localX, int localY, MouseButton button) {
			if (button.getName().equals(MouseButton.LEFT_BUTTON_NAME)) {
				w.requestFocus();
			}
		}
	}
}
