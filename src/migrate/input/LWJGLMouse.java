package migrate.input;

import static org.lwjgl.input.Mouse.create;
import static org.lwjgl.input.Mouse.getEventButton;
import static org.lwjgl.input.Mouse.getEventButtonState;
import static org.lwjgl.input.Mouse.getEventDWheel;
import static org.lwjgl.input.Mouse.getEventDX;
import static org.lwjgl.input.Mouse.getEventDY;
import static org.lwjgl.input.Mouse.getEventX;
import static org.lwjgl.input.Mouse.getEventY;
import static org.lwjgl.input.Mouse.isCreated;
import static org.lwjgl.input.Mouse.next;

import java.awt.Rectangle;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class LWJGLMouse extends Mouse {
	private static LWJGLMouse s_instance;

	{
		add(new MouseButton(0, MouseButton.LEFT_BUTTON_NAME));
		add(new MouseButton(1, MouseButton.RIGHT_BUTTON_NAME));
		add(new MouseButton(2, MouseButton.MIDDLE_BUTTON_NAME));
	}
	//Had to be added on migration
	private Rectangle m_displayShape = null;

	private LWJGLMouse() {}

	public void setGrabbed(boolean grabbed) { org.lwjgl.input.Mouse.setGrabbed(grabbed); }
	public void setX(int x) { org.lwjgl.input.Mouse.setCursorPosition(x, getY()); }
	public void setY(int y) { org.lwjgl.input.Mouse.setCursorPosition(getX(), y); }
	public void setDisplayShape(Rectangle rect) { m_displayShape = rect; }
	
	public void poll() {
		if (m_displayShape == null) throw new RuntimeException("Must call setDisplayShape() first!");
		if (!isCreated() && Display.isCreated()) {
			try {
				create();
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		}
		org.lwjgl.input.Mouse.poll();
		while (next()) {
			int buttonId = getEventButton();
			int dWheel = getEventDWheel();
			int dx = getEventDX();
			//Flip the delta y to match coordinate system
			int dy = -getEventDY();
			if (dx != 0 || dy != 0) updateDelta(dx, dy);
			if (buttonId != -1) {
				boolean state = getEventButtonState();
				updateButtonState(getMouseButton(buttonId), state);
			} else if (dWheel != 0) { mouseWheelMoved(dWheel);
			} else {
				int ex = getEventX();
				//Convert to match coordinate system
				int ey = (int) m_displayShape.getHeight() - getEventY();
				updateMousePosition(ex, ey);
			}
		}
	}
	
	public static LWJGLMouse getInstance() {
		if (s_instance == null) s_instance = new LWJGLMouse();
		return s_instance;
	}
}
