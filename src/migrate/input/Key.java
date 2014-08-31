package migrate.input;

/**
 * An immutable class with all the data of a key
 * This class does not actually store if the key is down
 */
public class Key {
	private char m_char;
	private String m_name;
	private int m_id;
	
	public Key(char c, String name, int id) {
		m_char = c;
		m_name = name;
		m_id = id;
	}
	public Key(char c, int id) {
		this(c, Character.toString(c), id);
	}
	
	/**
	 * @return the char
	 */
	public char getChar() {
		return m_char;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return m_name;
	}
	/**
	 * @return the id
	 */
	public int getID() {
		return m_id;
	}
	
	@Override
	public String toString() {
		if (getChar() != '\0') return getID() + ":" + getName() + ":" + getChar();
		else return getID() + ":" + getName(); 
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Key) {
			Key key = (Key) o;
			return (key.getChar() == m_char) && (key.getName().equals(m_name)) && (m_id == key.getID());
		} else return false;
	}
	@Override
	public int hashCode() {
		return m_id;
	}
}
