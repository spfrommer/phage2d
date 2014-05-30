package engine.core.implementation.behavior.activity;

import java.util.List;

import engine.core.framework.Aspect;
import engine.core.framework.AspectActivity;
import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.logic.TreeLogic;

public class BehaviorActivity extends AspectActivity {
	private ComponentType m_treeType;

	{
		m_treeType = TypeManager.getType(TreeLogic.class);
	}

	public BehaviorActivity(EntitySystem system) {
		super(system, new Aspect(TypeManager.getType(TreeLogic.class)));
	}

	public void update(int ticks) {
		List<Entity> entities = super.getEntities();

		for (Entity entity : entities) {
			TreeLogic tree = (TreeLogic) entity.getComponent(m_treeType);
			tree.update(ticks);
		}
	}

	@Override
	public void entityAdded(Entity entity) {

	}

	@Override
	public void entityRemoved(Entity entity) {

	}
}
