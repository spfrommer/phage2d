package engine.inputs.keyboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import engine.inputs.keyboard.config.XMLKeyConfigLoader;

public class KeyboardLayout {
	HashMap<String, Key> m_keyNames = new HashMap<String, Key>();
	HashMap<Character, Key> m_keyChars = new HashMap<Character, Key>();
	HashMap<Integer, Key> m_keyIDs = new HashMap<Integer, Key>();
	
	private ShiftMapping m_shiftMapping;
	
	public KeyboardLayout() {}
	public KeyboardLayout(ShiftMapping shiftMapping) {
		m_shiftMapping = shiftMapping;
	}
	public void setShiftMapping(ShiftMapping mapping) {
		m_shiftMapping = mapping;
	}
	public ShiftMapping getShiftMapping() {
		return m_shiftMapping;
	}
	
	public void addKey(Key key) {
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
	
	public void addKeysTo(Keyboard keyboard) {
		for (Key key : m_keyIDs.values()) {
			keyboard.addKey(key);
		}
	}

	public static class ShiftMapping {
		private HashMap<String, String> m_shiftMapping = new HashMap<String, String>();

		public void addMapping(String nonShift, String shift) {
			m_shiftMapping.put(nonShift, shift);
		}
		public String getShiftedKey(String nonShift) {
			if (m_shiftMapping.containsKey(nonShift)) return m_shiftMapping.get(nonShift);
			else return nonShift;
		}
		public Key getShiftedKey(Key nonShift, Keyboard keyboard) {
			String r = getShiftedKey(nonShift.getName());
			return keyboard.getKey(r);
		}
	}
	
	
	public static KeyboardLayout getLocal() {
		KeyboardLayout layout = new KeyboardLayout();
		
		Set<Key> keys = null;
		//TODO: Loading based on keyboards, getDefaultLayout() in keyboard?
		try {
			keys = XMLKeyConfigLoader.s_parseKeys(KeyboardLayout.class.getResourceAsStream("/input/keys_lwjgl.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		layout.addAll(keys);
		
		//TODO: Move to config file
		ShiftMapping smap = new ShiftMapping();
		//TODO: Fix for new config
		smap.addMapping("SLASH", "QUESTION");
		smap.addMapping("1", "EXCLAMATION");
		//smap.addMapping("2", "At");
		smap.addMapping("3", "POUND");
		smap.addMapping("4", "DOLLAR");
		smap.addMapping("5", "PERCENT");
		//smap.addMapping("6", "Carrot");
		smap.addMapping("7", "AMPERSAND");
		smap.addMapping("8", "ASTERISK");
		smap.addMapping("9", "LPARENTS");
		smap.addMapping("0", "RPARENTS");
		smap.addMapping("APOSTROPHE", "QUOTE");
		smap.addMapping("GRAVE", "TILDE");
		
		layout.setShiftMapping(smap);
		
		return layout;
	}
}
