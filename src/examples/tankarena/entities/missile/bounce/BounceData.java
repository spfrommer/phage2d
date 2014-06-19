package examples.tankarena.entities.missile.bounce;

import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;

public class BounceData extends DataComponent {
	public int bounceCount;
	public int maxBounces;
	// the last entity bounced against
	public Entity bouncedAgainst;

	@Override
	public Component copy() {
		BounceData bounce = new BounceData();
		bounce.maxBounces = this.maxBounces;
		return bounce;
	}
}
