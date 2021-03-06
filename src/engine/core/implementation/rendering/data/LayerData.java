package engine.core.implementation.rendering.data;

import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;

/**
 * Contains the rendering layer of this Entity.
 */
public class LayerData extends DataComponent {
	public double layer;

	public LayerData() {
		super();
	}

	@Override
	public Component copy() {
		LayerData layer = new LayerData();
		layer.layer = this.layer;
		return layer;
	}
}
