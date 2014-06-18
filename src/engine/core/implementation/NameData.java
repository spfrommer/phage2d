package engine.core.implementation;

import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;

/**
 * Holds a String name of the Component.
 */
public class NameData extends DataComponent {
	public String name;

	public NameData() {
		super();
	}

	@Override
	public Component copy() {
		NameData name = new NameData();
		name.name = this.name;
		return name;
	}

}
