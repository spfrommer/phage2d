package engine.core.implementation.extras.data;

import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;

/**
 * Contains the health of this Entity.
 */
public class HealthData extends DataComponent {
	public double health;
	// health recharge per second
	public double healthRechargeRate;

	public HealthData() {
		super();
	}

	@Override
	public Component copy() {
		HealthData health = new HealthData();
		health.health = this.health;
		return health;
	}

}
