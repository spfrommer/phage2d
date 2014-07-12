package migrate.gui;

public class FocusManager {
	private static FocusManager s_instance;

	private Widget m_focused = null;
	
	private FocusManager() {}
	
	public void requestFocus(Widget w) {
		m_focused = w;
	}
	public Widget getFocused() {
		return m_focused;
	}
	public boolean isFocused(Widget w) {
		return m_focused == w;
	}
	public static FocusManager getInstance() {
		if (s_instance == null) s_instance = new FocusManager();
		return s_instance;
	}
}
