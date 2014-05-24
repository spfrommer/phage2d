package engine.inputs.keyboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Keyboard implements KeyListener {

	private List<KeyListener> m_listeners = new ArrayList<KeyListener>();

	HashMap<String, Key> m_keyNames = new HashMap<String, Key>();
	HashMap<Character, Key> m_keyChars = new HashMap<Character, Key>();
	HashMap<Integer, Key> m_keyIDs = new HashMap<Integer, Key>();

	private KeyboardLayout m_layout;

	public Keyboard() {
		setLayout(KeyboardLayout.getLocal());
	}

	public Keyboard(KeyboardLayout layout) {
		setLayout(layout);
	}

	public KeyboardLayout getLayout() {
		return m_layout;
	}

	public void setLayout(KeyboardLayout layout) {
		m_layout = layout;
		removeAllKeys();
		m_layout.addKeysTo(this);
	}

	public void addKey(Key key) {
		key.addKeyListener(this);
		m_keyNames.put(key.getName(), key);
		m_keyChars.put(Character.toLowerCase(key.getCharacter()), key);
		m_keyIDs.put(key.getID(), key);
	}

	public void addAll(Collection<? extends Key> keys) {
		for (Key key : keys) {
			addKey(key);
		}
	}

	public void removeKey(Key key) {
		key.removeKeyListener(this);
		m_keyNames.remove(key.getName());
		m_keyChars.remove(Character.toLowerCase(key.getCharacter()));
		m_keyIDs.remove(key.getID());
	}

	public void removeAll(Collection<? extends Key> keys) {
		for (Key key : keys) {
			removeKey(key);
		}
	}

	public void removeAllKeys() {
		m_keyNames.clear();
		m_keyChars.clear();
		m_keyIDs.clear();
	}

	public Key getKey(String name) {
		return m_keyNames.get(name);
	}

	public Key getKey(char c) {
		return m_keyChars.get(Character.toLowerCase(c));
	}

	public Key getKey(int id) {
		return m_keyIDs.get(id);
	}

	public boolean hasKey(String name) {
		return m_keyNames.containsKey(name);
	}

	public boolean hasKey(char c) {
		return m_keyChars.containsKey(Character.toLowerCase(c));
	}

	public boolean hasKey(int id) {
		return m_keyIDs.containsKey(id);
	}

	public Collection<? extends Key> getKeys() {
		return m_keyIDs.values();
	}

	public boolean isKeyPressed(Key key) {
		return key.isPressed();
	}

	public boolean isKeyPressed(char key) {
		return getKey(key).isPressed();
	}

	public void addKeyEventListener(KeyListener listener) {
		m_listeners.add(listener);
	}

	public void removeKeyEventListener(KeyListener listener) {
		m_listeners.remove(listener);
	}

	public void onKeyEvent(KeyEvent event) {
		for (KeyListener listener : m_listeners) {
			listener.onKeyEvent(event);
		}
	}
}
