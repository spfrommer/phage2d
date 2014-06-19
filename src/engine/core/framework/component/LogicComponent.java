package engine.core.framework.component;

import engine.core.framework.Aspect;
import engine.core.framework.component.type.ComponentType;

/**
 * A Component that should only contain logic methods and other DataComponents or WrapperComponents that it is dependent
 * on as instance variables. Sometimes, certain data can be stored as an instance variable which isn't needed by the
 * entire Entity.
 */
public abstract class LogicComponent extends DependentComponent {
	public LogicComponent() {
		super();
	}

	public LogicComponent(Aspect dependencies) {
		super(filterAspect(dependencies));
	}

	@Override
	protected Component loadDependency(ComponentType type) {
		filterAspect(new Aspect(type));
		return this.getEntity().getComponent(type);
	}

	/**
	 * Filters all dependent Components to make sure there are only DataComponents and LogicComponents
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

			if (!DataComponent.class.isAssignableFrom(fullClass) && !WrapperComponent.class.isAssignableFrom(fullClass)) {
				throw new RuntimeException(
						"LogicComponent can only depend on / load Data/WrapperComponents!  Trying to access: "
								+ fullClass.getCanonicalName());
			}
		}
		return aspect;
	}
}