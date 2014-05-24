package engine.inputs.keyboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Key implements KeyListener {
	protected List<KeyListener> m_listeners = new ArrayList<KeyListener>();
	//
	private int m_id;
	private char m_character;
	private String m_name;

	private Set<KeyModifier> m_modifiers = new HashSet<KeyModifier>();
	// state
	private boolean m_down = false;

	public Key(Key key) {
		System.out.println("Key: " + key + " is being cloned");
		inherit(key);
	}

	public Key(int id, char character, String name) {
		setName(name);
		setID(id);
		setCharacter(character);
	}

	public Key(int id, char ch) {
		this(id, ch, Character.toString(ch));
	}

	public Key() {
	}

	public void inherit(Key key) {
		setName(key.getName());
		setID(key.getID());
		setCharacter(key.getCharacter());
		m_modifiers.addAll(key.getModifiers());
		m_listeners.addAll(key.getListeners());
	}

	public void addModifier(KeyModifier modifier) {
		m_modifiers.add(modifier);
	}

	public Set<KeyModifier> getModifiers() {
		return m_modifiers;
	}

	public void removeModifier(KeyModifier modifier) {
		m_modifiers.remove(modifier);
	}

	public void addKeyListener(KeyListener listener) {
		m_listeners.add(listener);
	}

	public void removeKeyListener(KeyListener listener) {
		m_listeners.remove(listener);
	}

	public Collection<KeyListener> getListeners() {
		return m_listeners;
	}

	public void setID(int id) {
		m_id = id;
	}

	public void setCharacter(char character) {
		m_character = character;
	}

	public void setName(String name) {
		m_name = name;
	}

	public void setPressed(boolean pressed) {
		m_down = pressed;
	}

	public boolean isPressed() {
		return m_down;
	}

	public int getID() {
		return m_id;
	}

	public char getCharacter() {
		return m_character;
	}

	public String getName() {
		return m_name;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Key) {
			Key key = (Key) o;
			return (key.getName().equals(getName()) && key.getCharacter() == getCharacter() && key.getID() == getID());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getID();
	}

	@Override
	public String toString() {
		if (getCharacter() != '\0')
			return getID() + ":" + getName() + ":" + getCharacter();
		else
			return getID() + ":" + getName();
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		if (event.getKey().equals(this)) {
			if (event instanceof KeyPressedEvent) {
				setPressed(true);
			} else if (event instanceof KeyReleasedEvent) {
				setPressed(false);
			}
			// if (m_name.equals("LSHIFT")) System.out.println(m_listeners.size() + " listeners");
			for (KeyListener listener : m_listeners) {
				// if (m_name.equals("LSHIFT")) System.out.println(this + " notifying listener: " + listener +
				// " out of " + m_listeners.size() + " about: " + event);
				listener.onKeyEvent(event);
			}
		}
	}

	@Override
	public Object clone() {
		return new Key(this);
	}

	public interface KeyModifier {
		public char modify(Keyboard keyboard, char c);
	}

	public static class ShiftKeyModifier implements KeyModifier {
		@Override
		public char modify(Keyboard keyboard, char c) {
			char caps = Character.toUpperCase(c);
			KeyboardLayout layout = keyboard.getLayout();

			if (keyboard.hasKey(c)) {
				Key key = keyboard.getKey(c);
				Key shifted = layout.getShiftMapping().getShiftedKey(key, keyboard);
				return shifted.getCharacter();
			}
			return caps;
		}
	}

	/**
	 * A key that can process events from multiple keys
	 */
	public static class MultiKey extends Key {
		private Set<Key> m_keys = new HashSet<Key>();

		public MultiKey(MultiKey key) {
			super(key);
			addAll(key.getKeys());
		}

		public MultiKey(int id, char c, String name) {
			super(id, c, name);
		}

		public MultiKey(int id, char c) {
			super(id, c);
		}

		public MultiKey() {
		}

		public void addAll(Collection<? extends Key> keys) {
			for (Key key : keys) {
				addKey(key);
			}
		}

		public void addKey(Key key) {
			key.addKeyListener(this);// We will now also listen for key events from that key
			m_keys.add(key);
		}

		public void removeKey(Key key) {
			key.removeKeyListener(key);
			m_keys.remove(key);
		}

		public Set<Key> getKeys() {
			return m_keys;
		}

		private void updatePressed() {
			for (Key key : m_keys) {
				if (key.isPressed()) {
					setPressed(true);
					return;
				}
			}
			setPressed(false);
		}

		@Override
		public void onKeyEvent(KeyEvent event) {
			KeyEvent newEvent = event.copy();
			newEvent.setKey(this);
			updatePressed();
			for (KeyListener listener : m_listeners) {
				listener.onKeyEvent(newEvent);
			}
		}

		@Override
		public Object clone() {
			return new MultiKey(this);
		}
	}
}
