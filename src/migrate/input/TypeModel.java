package migrate.input;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeModel implements KeyListener {
	private static final Logger logger = LoggerFactory.getLogger(TypeModel.class); 

	private HashSet<TypeListener> m_listeners = new HashSet<TypeListener>();
	
	//Used keys stores the keys that have been re-used
	//between combinations so that a keyTyped won't be fired
	private HashSet<Key> m_usedKeys = new HashSet<Key>();
	
	private ArrayList<Key> m_pressedKeys = new ArrayList<Key>();
	//Same as pressedKeys, but only for special keys
	private ArrayList<Key> m_pressedSpecialKeys = new ArrayList<Key>();
	
	private Combination m_currentCombination = new Combination();
	
	public void addTypeListener(TypeListener l) { m_listeners.add(l); }
	
	private boolean isModKey(Keyboard k, Key key) {
		return k.isModKey(key);
	}
	private boolean isShiftKey(Keyboard k, Key key) {
		return k.isShiftKey(key);
	} 
	
	@Override
	public void keyPressed(Keyboard keyboard, Key key) {
		m_currentCombination.add(key);
		if (key.getChar() != '\0') {
			finishCombination(keyboard);
			//Mark the special keys as already used
			m_usedKeys.addAll(m_pressedSpecialKeys);
			//And add them to the next combination
			m_currentCombination.addAll(m_pressedSpecialKeys);
		} else if (isModKey(keyboard, key) || isShiftKey(keyboard, key)) {
			m_pressedSpecialKeys.add(key);
		} else {
			for (TypeListener l : m_listeners) l.keyTyped(key);
		}
		m_pressedKeys.add(key);
	}
	@Override
	public void keyReleased(Keyboard keyboard, Key key) {
		m_pressedKeys.remove(key);
		//Remove it from the pressedSpecialKeys
		if (m_pressedSpecialKeys.contains(key))
			m_pressedSpecialKeys.remove(key);
		
		if (m_pressedKeys.size() == 0 && m_currentCombination.size() != 0) {
			if (m_currentCombination.size() == 1) {
				Key k = m_currentCombination.get(0);
				if (!m_usedKeys.contains(k))
					for (TypeListener l : m_listeners) l.keyTyped(k);
			} else {
				finishCombination(keyboard);
			}
			m_currentCombination.clear();
			m_usedKeys.clear();
		//If it is a mod key or a shift key, we need to remove the key from the combo
		} else if (isModKey(keyboard, key) || isShiftKey(keyboard, key)) {
			m_currentCombination.remove(key);
		}
	}
	private boolean hasModKeys(Keyboard keyboard, Combination combo) {
		for (Key k : combo.getKeys()) {
			if (isModKey(keyboard, k)) return true;
		}
		return false;
	}
	private char evaluateCombination(Keyboard keyboard, Combination combo) {
		char eval = '\0';
		ArrayList<Key> keys = combo.getKeys();
		for (int i = keys.size() - 1; i >= 0; i--) {
			Key key = keys.get(i);
			if (isShiftKey(keyboard, key)) {
				eval = Character.toUpperCase(eval);
				//Ignore mod keys
			} else if (!isModKey(keyboard, key) && key.getChar() != '\0') {
				eval = Character.toLowerCase(key.getChar());
			}
		}
		return eval;
	}
	/**
	 * Finished the combination and fires the listeners
	 * @param keyboard the keyboard with which to evaluate the combination
	 */
	public void finishCombination(Keyboard keyboard) {
		m_currentCombination.finish(evaluateCombination(keyboard, m_currentCombination));
		if (hasModKeys(keyboard, m_currentCombination)) {
			for (TypeListener l : m_listeners) l.comboFinished(m_currentCombination);
		} else {
			for (TypeListener l : m_listeners) l.characterTyped(m_currentCombination.eval());
		}
		m_currentCombination = new Combination();
	}
	
	
	public static class Combination {
		private ArrayList<Key> m_keys = new ArrayList<Key>();
		private char m_eval = '\0';
		private boolean m_finished = false;

		public int size() { return m_keys.size(); }
		public boolean isFinished() { return m_finished; }
		public ArrayList<Key> getKeys() { return m_keys; }
		
		public void add(Key key) {
			if (m_finished) throw new RuntimeException("Combination finished, cannot modify!");
			m_keys.add(key);
		}
		public void addAll(Collection<? extends Key> keys) { 
			if (m_finished) throw new RuntimeException("Combination finished, cannot modify!");
			m_keys.addAll(keys);
		}
		public void remove(Key key) {
			if (m_finished) throw new RuntimeException("Combination finished, cannot modify!");
			m_keys.remove(key);
		}
		public Key get(int i) {
			return m_keys.get(i);
		}
		public char eval() {
			if (!m_finished) throw new RuntimeException("Combination must be finished!");
			return m_eval;
		}
		public void finish(char eval) {
			m_finished = true;
			m_eval = eval;
		}
		
		public void clear() {
			m_keys.clear();
			m_eval = '\0';
			m_finished = false;
		}
		public String toString() {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < m_keys.size(); i++) {
				builder.append(m_keys.get(i).getName());
				if (i != m_keys.size() - 1) builder.append('-');
			}
			return builder.toString();
		}
	}
	public interface TypeListener {
		public void characterTyped(char c);
		public void keyTyped(Key k);
		public void comboFinished(Combination combo);
	}
}