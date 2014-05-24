package engine.core.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import engine.core.framework.component.type.ComponentType;

/**
 * Contains a List of ComponentTypes for determining which entities go to which process
 */
public class Aspect {
	private List<ComponentType> m_types;

	{
		m_types = new ArrayList<ComponentType>();
	}

	/**
	 * Constructs an Aspect with no types.
	 */
	public Aspect() {
	}

	/**
	 * Constructs an Aspect with one type.
	 * 
	 * @param type
	 */
	public Aspect(ComponentType type) {
		m_types.add(type);
	}

	/**
	 * Constructs an Aspect with multiple types.
	 * 
	 * @param componentTypes
	 */
	public Aspect(ComponentType... componentTypes) {
		for (ComponentType type : componentTypes) {
			m_types.add(type);
		}
	}

	/**
	 * Adds a type to the Aspect.
	 * 
	 * @param type
	 * @return this Aspect
	 */
	public Aspect addType(ComponentType type) {
		if (!m_types.contains(type))
			m_types.add(type);
		return this;
	}

	/**
	 * Adds types to the Aspect.
	 * 
	 * @param types
	 * @return this Aspect
	 */
	public Aspect addTypes(ComponentType[] types) {
		for (ComponentType type : types) {
			if (!m_types.contains(type))
				m_types.add(type);
		}
		return this;
	}

	/**
	 * Adds types to the Aspect.
	 * 
	 * @param types
	 * @return this Aspect
	 */
	public Aspect addTypes(List<ComponentType> types) {
		for (ComponentType type : types) {
			if (!m_types.contains(type))
				m_types.add(type);
		}
		return this;
	}

	/**
	 * Adds all the types in another Aspect.
	 * 
	 * @param aspect
	 * @return this Aspect
	 */
	public Aspect addTypes(Aspect aspect) {
		return addTypes(aspect.getTypes());
	}

	/**
	 * Removes a type from the Aspect.
	 * 
	 * @param type
	 * @return this Aspect
	 */
	public Aspect removeType(ComponentType type) {
		m_types.remove(type);
		return this;
	}

	/**
	 * Removes types from the Aspect.
	 * 
	 * @param types
	 * @return this Aspect
	 */
	public Aspect removeTypes(ComponentType[] types) {
		for (ComponentType type : types)
			m_types.remove(type);
		return this;
	}

	/**
	 * Removes types from the Aspect.
	 * 
	 * @param types
	 * @return this Aspect
	 */
	public Aspect removeTypes(ArrayList<ComponentType> types) {
		for (ComponentType type : types)
			m_types.remove(type);
		return this;
	}

	/**
	 * Checks if this Aspect contains a certain ComponentType.
	 * 
	 * @param type
	 * @return whether this Aspect contains the type.
	 */
	public boolean hasType(ComponentType type) {
		return m_types.contains(type);
	}

	/**
	 * Gets all the Types in this Aspect in an unmodifyable list.
	 * 
	 * @return
	 */
	public List<ComponentType> getTypes() {
		return Collections.unmodifiableList(m_types);
	}

	/**
	 * Checks if all the ComponentTypes in this Aspect match those in the other.
	 * 
	 * @param aspect
	 * @return whether the types match
	 */
	public boolean matches(Aspect aspect) {
		if (m_types.size() != aspect.getTypes().size())
			return false;

		return m_types.containsAll(aspect.getTypes()) && aspect.getTypes().containsAll(m_types);
	}

	/**
	 * Checks if this Aspect contains all the ComponentTypes in another Aspect.
	 * 
	 * @param aspect
	 * @return if all types are contained
	 */
	public boolean encapsulates(Aspect aspect) {
		for (ComponentType type : aspect.getTypes()) {
			if (!m_types.contains(type))
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < m_types.size(); i++) {
			builder.append(m_types.get(i).toString());
			if (i < m_types.size())
				builder.append(", ");
		}
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_types == null) ? 0 : m_types.hashCode());
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
		Aspect other = (Aspect) obj;
		if (m_types == null) {
			if (other.m_types != null)
				return false;
		} else {
			for (ComponentType type : m_types) {
				if (!other.hasType(type))
					return false;
			}
			for (ComponentType type : other.getTypes()) {
				if (!this.hasType(type))
					return false;
			}
		}
		return true;
	}
}
