package migrate.gui.widgets;

import migrate.gui.Widget;
import migrate.input.Mouse;
import migrate.input.Mouse.MouseButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Button extends Widget {
	private static final Logger logger = LoggerFactory.getLogger(Button.class);
	
	private boolean m_down = false; 
	
	@Override
	public void mouseButtonPressed(Mouse m, int localX, int localY, MouseButton button) {
		m_down = true;
		logger.info("Button pressed!");
	}
	@Override
	public void mouseButtonReleased(Mouse m, int localX, int localY, MouseButton button) {
		m_down = false;
		logger.info("Button released!");
	}
}
