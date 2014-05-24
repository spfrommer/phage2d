package engine.inputs.keyboard;

public class KeyTypedEvent extends KeyEvent {
	Key m_key;
	
	public KeyTypedEvent(Key key) {
		m_key = key;
	}
	
	public Key getKey() { return m_key; }
	public void setKey(Key key) { m_key = key; }
	
	public KeyEvent copy() { return new KeyTypedEvent(getKey()); }
}
