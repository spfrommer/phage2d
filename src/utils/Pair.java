package utils;

public class Pair<K, V> {

	 /**
	  * The 'key' element of this <code>Pair</code>
	  */
	    private K m_key;

	    /**
	     * The 'value' of this <code>Pair</code>
	     */
	    private V m_value;

	    /**
	     * Constructs a new <code>Pair</code> with the given values.
	     * 
	     * @param key  the key
	     * @param value the value
	     */
	    public Pair(K key, V value) {
	    	setKey(key);
	    	setValue(value);
	    }
	    
	    public void setKey(K key) 		{ m_key = key; }
	    public void setValue(V value) 	{ m_value = value; }
	    public K getKey() 		{ return m_key;  }
	    public V getValue() 	{ return m_value; }
	    
	    @Override
	    public int hashCode() {
	    	return getKey().hashCode() + getValue().hashCode();
	    }
	    
	    @Override
	    public boolean equals(Object o) {
	    	if (o instanceof Pair) {
	    		Pair<?, ?> p = (Pair<?, ?>) o;
	    		if (p.getKey().equals(getKey()) && p.getValue().equals(getValue())) return true;
	    	}
	    	return false;
	    }
}