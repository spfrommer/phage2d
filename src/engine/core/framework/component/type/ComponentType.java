package engine.core.framework.component.type;

/**
 * Contains the type integer corresponding to a certain base component - see TypeManager for details.
 */
public class ComponentType {
	/**
	 * The type integer
	 */
	private int m_type;

	/**
	 * The full name of the class
	 */
	private String m_typeName;

	/**
	 * Protected constructor to prevent rogue types
	 * 
	 * @param type
	 * @param typeName
	 */
	protected ComponentType(int type, String typeName) {
		m_type = type;
		m_typeName = typeName;
	}

	public int getType() {
		return m_type;
	}

	@Override
	public String toString() {
		return m_typeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + m_type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComponentType other = (ComponentType) obj;
		if (m_type != other.m_type)
			return false;
		return true;
	}
}
