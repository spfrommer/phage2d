package engine.core.framework.component.type;

import java.util.HashMap;
import java.util.Map;

import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;
import engine.core.framework.component.LogicComponent;
import engine.core.framework.component.WrapperComponent;

/**
 * Statically maps Component classes to types
 */
public class TypeManager {
	/**
	 * Current type integer
	 */
	private static int type_counter = 0;

	private static Map<Class<? extends Component>, ComponentType> types = new HashMap<Class<? extends Component>, ComponentType>();

	/**
	 * Will return one type per base component - extended components will still have the same type. eg, if
	 * TextureRenderingComponent and ParticleRenderingComponent both extend TextureRenderingComponent which extends
	 * LogicComponent; ParticleRenderingComponent and TextureRenderingComponent will have same type because each Entity
	 * can have only one rendering component. There will be only one ComponentType object per type.
	 * 
	 * @param compClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ComponentType getType(Class<? extends Component> compClass) {
		while (!((compClass.getSuperclass().equals(DataComponent.class)
				|| compClass.getSuperclass().equals(LogicComponent.class) || compClass.getSuperclass().equals(
				WrapperComponent.class)))) {
			compClass = (Class<? extends Component>) compClass.getSuperclass();
		}
		ComponentType type = types.get(compClass);

		if (type == null) {
			type = new ComponentType(type_counter++, compClass.getName());
			types.put(compClass, type);
		}

		return type;
	}
}
