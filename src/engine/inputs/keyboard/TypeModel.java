package engine.inputs.keyboard;

import java.util.ArrayList;

import engine.inputs.keyboard.Key.KeyModifier;
import engine.inputs.keyboard.Key.MultiKey;

/**
 * Responsible for modifiers such as shift, and in the future keyboard combinations
 */
//TODO: Keyboard combinations
public class TypeModel {
	private ArrayList<TypeListener> m_listeners = new ArrayList<TypeListener>();
	
	/**
	 * Pressed keys differs from combination keys in that combination keys
	 * has all the keys that were pressed without pressedKeys.length() going to zero
	 * Also, combination keys clears itself if a non-modifier key gets pressed
	 */
	private ArrayList<Key> m_pressedKeys = new ArrayList<Key>();
	private ArrayList<Key> m_combinationKeys = new ArrayList<Key>();

	private Keyboard m_keyboard;
	public TypeModel() {}
	public TypeModel(Keyboard keyboard) {
		m_keyboard = keyboard;
	}
	
	public void setKeyboard(Keyboard keyboard) { m_keyboard = keyboard; }
	
	public void process(KeyEvent event) {
		if (event instanceof KeyPressedEvent) {
			Key key = event.getKey();
			//Will make it prefer multikeys over sub-keys
			for (Key k : m_combinationKeys) {
				if (k instanceof MultiKey) {
					MultiKey multi = (MultiKey) k;
					//A multi key is already in the event, so ignore the sub-key, but still check if it is a special character
					if (multi.getKeys().contains(key)) {
						if (key.getCharacter() == '\0') {
							//Special character
							for (TypeListener listener : m_listeners) {
								listener.specialKey(key);
							}
						}
						return;
					}
				}
			}
			//Check to make sure we don't already have this key
			if (m_combinationKeys.contains(key)) return;
			
			m_pressedKeys.add(key);
			
			m_combinationKeys.add(key);
			if (key.getCharacter() == '\0') {
				//Special character
				for (TypeListener listener : m_listeners) {
					listener.specialKey(key);
				}
			} else if (key.getModifiers().size() == 0) {
				//This key is not a modifier, so end the combination
				combinationFinished();
				//This will allow the previous keys to be re-used in another combination
				m_combinationKeys.remove(key);
			}
		} else if (event instanceof KeyReleasedEvent) {
			m_pressedKeys.remove(event.getKey());
			if (m_pressedKeys.size() == 0) {
				//If there are no keys being pressed, and a key just was lifted, clear the combinationKeys
				m_combinationKeys.clear();
			}
		}
	}
	public void combinationFinished() {
		char eval = evaluate(m_combinationKeys);
		for (TypeListener listener : m_listeners) {
			listener.typed(eval);
		}
	}
	/**
	 * Does the actual key --> Character transformation
	 * @param key
	 * @return
	 */
	public char evaluate(ArrayList<Key> keys) {
		//Loop backwards so that the ending key will be applied to the char first
		char eval = '\0';
		for (int i = keys.size() - 1; i >= 0; i--) {
			Key key = keys.get(i);
			if (key.getModifiers().size() > 0) {
				for (KeyModifier mod : key.getModifiers()) {
					eval = mod.modify(m_keyboard, eval);
				}
			} else {
				if (key.getCharacter() != '\0') {
					eval = Character.toLowerCase(key.getCharacter());
				}
			}
		}
		return eval;
	}
	
	public void addListener(TypeListener l) {
		m_listeners.add(l);
	}
	public void removeListener(TypeListener l) {
		m_listeners.remove(l);
	}
	
	public interface TypeListener {
		//For keys with no char
		public void specialKey(Key key);
		public void typed(char c);
	}
	}
