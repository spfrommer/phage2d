package engine.core.implementation.extras.data;

import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;

/**
 * Contains this Entity deals (for bullets / missiles).
 */
public class DamageData extends DataComponent {
	public double damage;

	public DamageData() {
		super();
	}

	public DamageData(Entity parent) {
		super(parent);
	}

	@Override
	public Component copy() {
		DamageData damage = new DamageData();
		damage.damage = this.damage;
		return damage;
	}

}
