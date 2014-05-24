package engine.gui;

public class FocusManager {
	private Focusable m_focused;
	
	public void requestFocus(Focusable focus) {
		setFocusedItem(focus);
	}
	public void setFocusedItem(Focusable focus) {
		if (m_focused != null) m_focused.setFocused(false);
		focus.setFocused(true);
		//We have now changed the focus
	}
}
