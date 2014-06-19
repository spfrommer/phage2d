package engine.core.framework.component;

import engine.core.framework.Aspect;
import engine.core.framework.component.type.ComponentType;

/**
 * A Component that relies on other Components.
 */
public abstract class DependentComponent extends Component {
	private Aspect m_dependencies;

	{
		m_dependencies = new Aspect();
	}

	/**
	 * Constructs a new Component with no dependencies.
	 */
	DependentComponent() {
		super();
	}

	/**
	 * Constructs a new Component with a set of dependencies. If these dependencies are not met by the Entity, the
	 * Component won't be added to the Entity.
	 * 
	 * @param dependencies
	 */
	DependentComponent(Aspect dependencies) {
		super();
		m_dependencies = dependencies;
	}

	/**
	 * Returns the Components that this Component needs to do its job.
	 * 
	 * @return
	 */
	public Aspect getDependencies() {
		return m_dependencies;
	}

	/**
	 * Loads another Component from the Entity.
	 * 
	 * @param type
	 * @return
	 */
	protected Component loadDependency(ComponentType type) {
		return this.getEntity().getComponent(type);
	}

	/**
	 * Call from the Entity for this Component to load all required dependencies.
	 */
	public abstract void loadDependencies();
}
