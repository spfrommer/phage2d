package engine.core.framework;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;
import engine.core.framework.component.DependentComponent;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.NameData;

/**
 * Simply a bag of logic and data components.
 */
public class Entity {
	/**
	 * The combined Aspect of all the Components in this Entity.
	 */
	private Aspect m_aspect;

	/**
	 * Contains all the Components in this Entity. Also maps a ComponentType to a Component for quick retrieval.
	 */
	private Map<ComponentType, Component> m_components;

	{
		m_aspect = new Aspect();
		m_components = new HashMap<ComponentType, Component>();
	}

	/**
	 * Constructs an empty Entity.
	 */
	public Entity() {
	}

	/**
	 * Makes a copy of this Entity by copying all of the Components and force adding them into a new Entity.
	 * 
	 * @return
	 */
	public Entity copy() {
		Entity entity = new Entity();
		for (Component comp : getComponents()) {
			Component copy = comp.copy();
			copy.setEntity(entity);
			entity.forceAddComponent(copy);
		}

		entity.forceLoadDependencies();
		return entity;
	}

	/**
	 * Adds a Component to this Entity and sets the Component's parent to this Entity. Only one Component of each
	 * ComponentType allowed. If it's a LogicComponent, there will also be checking to ensure that all dependencies are
	 * satisfied.
	 * 
	 * @param comp
	 */
	public void addComponent(Component comp) {
		if (!(comp instanceof DataComponent || comp instanceof DependentComponent))
			throw new RuntimeException("Component not instance of Data or Dependent Component");

		if (!m_components.containsKey(TypeManager.getType(comp.getClass()))) {
			if (comp instanceof DependentComponent)
				checkDependencies((DependentComponent) comp);

			m_aspect.addType(TypeManager.getType(comp.getClass()));
			m_components.put(TypeManager.getType(comp.getClass()), comp);
			comp.setEntity(this);

			if (comp instanceof DependentComponent)
				((DependentComponent) comp).loadDependencies();
		} else {
			throw new RuntimeException("Already a component for: " + comp);
		}

	}

	private void checkDependencies(DependentComponent comp) {
		if (!m_aspect.encapsulates(comp.getDependencies())) {
			for (ComponentType type : comp.getDependencies().getTypes()) {
				if (!m_aspect.hasType(type))
					System.err.println("Can't find type: " + type + " for " + comp);
			}
			throw new RuntimeException("Dependencies: " + comp.getDependencies() + " not fulfilled for: " + comp);
		}
	}

	// should never be needed
	/*public void addMutuallyDependentComponents(Component[] components) {
		for (Component comp : components)
			m_aspect.addType(TypeManager.getType(comp.getClass()));

		for (Component comp : components) {
			if (!m_components.containsKey(TypeManager.getType(comp.getClass()))
					&& m_aspect.encapsulates(comp.getDependencies())) {
				m_components.put(TypeManager.getType(comp.getClass()), comp);
			} else {
				throw new RuntimeException("Dependencies not fulfilled for: " + comp);
			}
		}

		for (Component comp : components) {
			comp.loadDependencies();
		}
	}*/

	/**
	 * Adds a component without regard to dependencies.
	 * 
	 * @param comp
	 */
	private void forceAddComponent(Component comp) {
		m_aspect.addType(TypeManager.getType(comp.getClass()));
		m_components.put(TypeManager.getType(comp.getClass()), comp);
	}

	/**
	 * Calls on all DependentComponents to reload their dependencies.
	 */
	private void forceLoadDependencies() {
		for (Component c : getComponents()) {
			if (c instanceof DependentComponent)
				((DependentComponent) c).loadDependencies();
		}
	}

	/**
	 * Gets a Component of a certain type.
	 * 
	 * @param type
	 * @return
	 */
	public Component getComponent(ComponentType type) {
		return m_components.get(type);
	}

	/**
	 * Gets the Aspect of the Entity.
	 * 
	 * @return
	 */
	public Aspect getAspect() {
		return m_aspect;
	}

	/**
	 * Returns whether or not this Entity contains a Component of this type.
	 * 
	 * @param type
	 * @return
	 */
	public boolean hasComponent(ComponentType type) {
		return m_aspect.encapsulates(new Aspect(type));
	}

	/**
	 * Gets all the Components in the Entity.
	 * 
	 * @return
	 */
	public Collection<Component> getComponents() {
		return m_components.values();
	}

	@Override
	public String toString() {
		if (m_aspect.encapsulates(new Aspect(TypeManager.getType(NameData.class)))) {
			return ((NameData) this.getComponent(TypeManager.getType(NameData.class))).name;
		} else {
			return m_aspect.toString();
		}
	}
}
