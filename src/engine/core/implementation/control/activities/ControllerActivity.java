package engine.core.implementation.control.activities;

import java.util.List;

import engine.core.framework.Aspect;
import engine.core.framework.AspectActivity;
import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.control.logic.ControllerLogic;

/**
 * An Activity that runs ControllerLogics.
 * 
 * @eng.dependencies ControllerLogic
 */
public class ControllerActivity extends AspectActivity {
	private ComponentType m_controllerType;

	public ControllerActivity(EntitySystem system) {
		super(system, new Aspect(TypeManager.getType(ControllerLogic.class)));

		m_controllerType = TypeManager.getType(ControllerLogic.class);
	}

	@Override
	public void entityAdded(Entity entity) {

	}

	@Override
	public void entityRemoved(Entity entity) {

	}

	/**
	 * Updates all the Entities that have a ControllerLogic.
	 * 
	 * @param timeNanos
	 */
	public void update(int ticks) {
		List<Entity> entities = super.getEntities();
		for (Entity entity : entities) {
			ControllerLogic controller = (ControllerLogic) entity.getComponent(m_controllerType);
			controller.update(ticks);
		}
	}
}
