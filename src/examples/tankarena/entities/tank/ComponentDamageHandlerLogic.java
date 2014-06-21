package examples.tankarena.entities.tank;

import engine.core.framework.component.Component;
import engine.core.implementation.extras.logic.DamageHandlerLogic;

public class ComponentDamageHandlerLogic extends DamageHandlerLogic {
	private Tank m_tank;

	/**
	 * Sets the Tank this damage handler reports to.
	 * 
	 * @param tank
	 */
	public void setTank(Tank tank) {
		m_tank = tank;
	}

	/**
	 * Gets the Tank this damage handler reports to.
	 * 
	 * @return
	 */
	public Tank getTank() {
		return m_tank;
	}

	@Override
	public void handleDamage(double damage) {
		if (m_tank != null)
			m_tank.damage(damage);
	}

	@Override
	public void loadDependencies() {

	}

	@Override
	public Component copy() {
		return new ComponentDamageHandlerLogic();
	}
}
