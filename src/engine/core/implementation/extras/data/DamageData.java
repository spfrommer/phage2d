package engine.core.implementation.extras.data;

import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;

/**
 * Contains the damage this Entity deals (for bullets / missiles).
 */
public class DamageData extends DataComponent {
	public double damage;

	public DamageData() {
		super();
	}

	@Override
	public Component copy() {
		DamageData damage = new DamageData();
		damage.damage = this.damage;
		return damage;
	}

}
