package engine.inputs.mouse;

import engine.core.implementation.camera.base.ViewPort;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

public class JavaMouse extends Mouse {
	
	{
		MouseButton left = new MouseButton(MouseButton.LEFT_NAME, 0);
		MouseButton right = new MouseButton(MouseButton.RIGHT_NAME, 1);
		MouseButton middle = new MouseButton(MouseButton.MIDDLE_NAME, 2);
		addMouseButton(left);
		addMouseButton(right);
		addMouseButton(middle);
	}
	
	public JavaMouse(Component toListen) {

		toListen.addMouseListener(new java.awt.event.MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e))
					setButtonState(getMouseButton(MouseButton.LEFT_NAME), true);
				if (SwingUtilities.isMiddleMouseButton(e))
					setButtonState(getMouseButton(MouseButton.MIDDLE_NAME), true);
				if (SwingUtilities.isRightMouseButton(e))
					setButtonState(getMouseButton(MouseButton.RIGHT_NAME), true);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e))
					setButtonState(getMouseButton(MouseButton.LEFT_NAME), false);
				if (SwingUtilities.isMiddleMouseButton(e))
					setButtonState(getMouseButton(MouseButton.MIDDLE_NAME), false);
				if (SwingUtilities.isRightMouseButton(e))
					setButtonState(getMouseButton(MouseButton.RIGHT_NAME), false);
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

		});
		toListen.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				get().mouseMoved(e.getX(), e.getY());
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				get().mouseMoved(e.getX(), e.getY());
			}
		});
	}

	// need get to get outer instance
	private JavaMouse get() {
		return this;
	}
}
