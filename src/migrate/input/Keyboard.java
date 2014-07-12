package migrate.input;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Keyboard {
	public static final String KEY_BACKSPACE = "BACKSPACE";
	public static final String KEY_ESCAPE = "ESCAPE";
	public static final String KEY_LEFT = "LEFT";
	public static final String KEY_RIGHT = "RIGHT";
	public static final String KEY_UP = "UP";
	public static final String KEY_DOWN = "DOWN";

	private HashSet<Key> m_shiftKeys = new HashSet<Key>();
	private HashSet<Key> m_modKeys = new HashSet<Key>();

	private HashMap<Integer, Key> m_keyIds = new HashMap<Integer, Key>();
	private HashMap<String, Key> m_keyNames = new HashMap<String, Key>();
	private HashMap<Key, Boolean> m_state = new HashMap<Key, Boolean>();
	
	private ArrayList<KeyListener> m_listeners = new ArrayList<KeyListener>();
	
	public void addModKey(Key key) { m_modKeys.add(key); }
	public void addShiftKey(Key key) { m_shiftKeys.add(key); }
	
	public void addKey(Key key) {
		m_keyIds.put(key.getID(), key);
		m_keyNames.put(key.getName(), key);
		m_state.put(key, false);
	}
	
	public void addAll(Collection<Key> keys) {
		for (Key k : keys) {
			addKey(k);
		}
	}
	
	public void addListener(KeyListener l) {
		m_listeners.add(l);
	}
	
	public void readXMLKeyConfig(InputStream stream) throws Exception {
		XMLKeyConfigLoader.s_parseConfig(stream, this);
	}
	
	public HashSet<Key> getModKeys() { return m_modKeys; }
	public HashSet<Key> getShiftKeys() { return m_shiftKeys; }
	
	public Key getKey(String name) {
		return m_keyNames.get(name);
	}
	public Key getKey(int id) {
		return m_keyIds.get(id);
	}
	public Key getKey(char c) {
		//Search for the key with the uppercase version of the char as its name
		String upper = Character.toString(Character.toUpperCase(c));
		return m_keyNames.get(upper);
	}
	
	public boolean isKeyPressed(Key k) {
		return m_state.get(k);
	}
	public boolean isModKey(Key k) {
		return m_modKeys.contains(k);
	}
	public boolean isShiftKey(Key k) {
		return m_shiftKeys.contains(k);
	}
	
	protected void updateState(Key k, boolean state) {
		if (k == null) return;
		m_state.put(k, state);
		if (state)
			for (KeyListener l : m_listeners) {
				l.keyPressed(this, k);
			}
		else
			for (KeyListener l : m_listeners) {
				l.keyReleased(this, k);
			}
	}
	
	//-----Abstract functions------
	
	public abstract void poll();
	
	//-----Triggers------
	
	public static class KeyPressedTrigger extends InputTrigger implements KeyListener {
		private Key m_key;
		public KeyPressedTrigger(Keyboard keyboard, Key key) {
			keyboard.addListener(this);
			m_key = key;
		}
		
		@Override
		public void keyPressed(Keyboard k, Key key) {
			if (key.equals(m_key)) {
				trigger(1.0f);
			}
		}
		@Override
		public void keyReleased(Keyboard k, Key key) {	}
	}
	public static class KeyReleasedTrigger extends InputTrigger implements KeyListener {
		private Key m_key;
		
		public KeyReleasedTrigger(Keyboard keyboard, Key key) {
			keyboard.addListener(this);
			m_key = key;
		}
		
		@Override
		public void keyPressed(Keyboard k, Key key) {}
		@Override
		public void keyReleased(Keyboard k, Key key) {	
			if (key.equals(m_key)) trigger(1.0f);
		}
	}
}
