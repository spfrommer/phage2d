package examples.spaceship.spawning.logic;

import java.util.List;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.LogicComponent;

/**
 * An abstract class called by the SpawningActivity to spawn Entities.
 */
public abstract class SpawningLogic extends LogicComponent {
	public SpawningLogic(Aspect dependencies) {
		super(dependencies);
	}

	/**
	 * Returns a List of spawned Entities that should be added.
	 * 
	 * @param ticks
	 * 
	 * @return
	 */
	public abstract List<Entity> spawn(int ticks);
}
