package migrate.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public abstract class Keyboard {
	private HashMap<Integer, Key> m_keyIds = new HashMap<Integer, Key>();
	private HashMap<String, Key> m_keyNames = new HashMap<String, Key>();
	private HashMap<Key, Boolean> m_state = new HashMap<Key, Boolean>();
	
	private ArrayList<KeyListener> m_listeners = new ArrayList<KeyListener>();
	

	
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
	
	public void readXMLKeyConfig(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
		addAll(XMLKeyConfigLoader.s_parseKeys(stream));
	}
	
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
