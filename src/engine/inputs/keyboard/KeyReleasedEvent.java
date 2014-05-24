package engine.inputs.keyboard;

public class KeyReleasedEvent extends KeyEvent {
	Key m_key;
	
	public KeyReleasedEvent(Key key) {
		m_key = key;
	}
	
	public Key getKey() { return m_key; }
	public void setKey(Key key) { m_key = key; }
	
	public KeyEvent copy() { return new KeyReleasedEvent(getKey()); }
}
