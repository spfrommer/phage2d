package engine.inputs.keyboard;

public class KeyPressedEvent extends KeyEvent {
	Key m_key;
	
	public KeyPressedEvent(Key key) {
		m_key = key;
	}
	
	public Key getKey() { return m_key; }
	public void setKey(Key key) { m_key = key; }
	
	public KeyEvent copy() { return new KeyPressedEvent(getKey()); }
}
