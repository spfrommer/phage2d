package engine.core.implementation.extras.logic;

import engine.core.framework.component.LogicComponent;

/**
 * An abstract class that allows specific Entities to handle how they receive damage. If you want to damage an Entity,
 * retrieve this Component and call handleDamage(double damage).
 */
public abstract class DamageHandlerLogic extends LogicComponent {
	/**
	 * Allows the subclass to handle a certain amount of damage.
	 * 
	 * @param damage
	 */
	public abstract void handleDamage(double damage);
}
