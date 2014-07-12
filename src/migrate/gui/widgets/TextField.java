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

	private StringBuilder m_text = new StringBuilder();
	private TypeModel m_model = new TypeModel();
	//The cursor position, which is the offset from the start
	//0 is at the beginning, 1 is after the first character, 2 after the second etc. etc.
	private int m_cursorPosition = 0;
	
	public TextField() {
		setFocusPolicy(new ClickFocusPolicy());
		m_model.addTypeListener(this);
	}
	public void setCursorPosition(int position) { m_cursorPosition = position; }
	
	public int getCursorPosition() { return m_cursorPosition; }
	public String getText() { return m_text.toString(); }
	public StringBuilder getTextBuilder() { return m_text; }
	public String getPrecursorText() { return getTextBuilder().substring(0, getCursorPosition()); }

	private void backspace() {
		int beforeCursor = m_cursorPosition - 1;
		if (beforeCursor > -1) {
			m_text.deleteCharAt(beforeCursor);
			m_cursorPosition--;
		}		
	}
	private void deletePressed() {
		int afterCursor = m_cursorPosition;
		if (afterCursor < m_text.length()) {
			m_text.deleteCharAt(afterCursor);
		}
	}

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
		m_text.insert(m_cursorPosition, c);
		m_cursorPosition++;
	}
	@Override
	public void keyTyped(Key k) {
		if (k.getName().equals(Keyboard.KEY_BACKSPACE)) {
			backspace();
		} else if (k.getName().equals(Keyboard.KEY_DELETE)) {
			deletePressed();
		} else if (k.getName().equals(Keyboard.KEY_LEFT)) {
			if (m_cursorPosition > 0) m_cursorPosition--;
		} else if (k.getName().equals(Keyboard.KEY_RIGHT)) {
			if (m_cursorPosition < m_text.length()) m_cursorPosition++;
		}
	}
	@Override
	public void comboFinished(Combination combo) {}
}
