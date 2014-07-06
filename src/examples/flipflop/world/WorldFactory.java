package examples.flipflop.world;

import engine.core.framework.EntitySystem;

public interface WorldFactory {
	public abstract void setWorld(EntitySystem system);
}
