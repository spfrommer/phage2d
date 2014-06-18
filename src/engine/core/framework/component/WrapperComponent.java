package engine.core.framework.component;

import engine.core.framework.Aspect;
import engine.core.framework.component.type.ComponentType;

/**
 * A Component that can only depend on DataComponents and acts as a wrapper to display this data in a different way to
 * the LogicComponents. This Component shouldn't modify any of its dependencies directly, or be called on directly to do
 * anything by an Activity. Often, a base WrapperComponent will be a collection of different abstract methods and
 * extending Wrappers will provide implementation based on DataComponents.
 */
public abstract class WrapperComponent extends DependentComponent {
	public WrapperComponent() {
		super();
	}

	public WrapperComponent(Aspect dependencies) {
		super(filterAspect(dependencies));
	}

	@Override
	protected Component loadDependency(ComponentType type) {
		filterAspect(new Aspect(type));
		return this.getEntity().getComponent(type);
	}

	/**
	 * Filters all dependent Components to make sure there are only DataComponents
	 * 
	 * @param aspect
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Aspect filterAspect(Aspect aspect) {
		for (ComponentType type : aspect.getTypes()) {
			Class<? extends Component> fullClass = null;
			try {
				fullClass = (Class<? extends Component>) Class.forName(type.toString());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if (!DataComponent.class.isAssignableFrom(fullClass)) {
				throw new RuntimeException(
						"WrapperComponent can only depend on / load DataComponents!  Trying to access: "
								+ fullClass.getCanonicalName());
			}
		}
		return aspect;
	}
}
