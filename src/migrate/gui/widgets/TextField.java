package migrate.gui.widgets;

import migrate.gui.FocusPolicy.ClickFocusPolicy;
import migrate.gui.Widget;
import migrate.input.Key;
import migrate.input.Keyboard;
import migrate.input.TypeModel;
import migrate.input.TypeModel.Combination;
import migrate.input.TypeModel.TypeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TextField extends Widget implements TypeListener {
	private static final Logger logger = LoggerFactory.getLogger(TextField.class); 

	private StringBuilder m_text;
	private TypeModel m_model = new TypeModel();
	//The cursor position, which is the offset from the start
	//0 is at the beginning, 1 is after the first character, 2 after the second etc. etc.
	private int m_cursorPosition = 0;
	
	public TextField() {
		setFocusPolicy(new ClickFocusPolicy());
		m_model.addTypeListener(this);
	}

	public String getText() { return m_text.toString(); }

	@Override
	public void keyPressed(Keyboard keyboard, Key key) {
		m_model.keyPressed(keyboard, key);
	}
	@Override
	public void keyReleased(Keyboard keyboard, Key key) {
		m_model.keyReleased(keyboard, key);
	}
	@Override
	public void characterTyped(char c) {
		logger.debug("Char typed: " + c);
	}
	@Override
	public void keyTyped(Key k) {
		logger.debug("Key typed: " + k);
	}
	@Override
	public void comboFinished(Combination combo) {
		logger.debug("Combo finished: " + combo);
	}
}
