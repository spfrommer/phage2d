package engine.core.implementation.rendering.logic;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.LogicComponent;
import engine.graphics.Renderer;

/**
 * An abstract RenderingLogic class that's called by the RenderingActivity.
 */
public abstract class RenderingLogic extends LogicComponent {
	public RenderingLogic(Aspect dependencies) {
		super(dependencies);
	}

	public RenderingLogic(Entity parent, Aspect dependencies) {
		super(parent, dependencies);
	}

	/**
	 * Renders this Entity.
	 * 
	 * @param renderer
	 */
	public abstract void render(Renderer renderer);
}
