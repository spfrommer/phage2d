package engine.core.implementation.behavior.activity;

import engine.core.framework.Aspect;
import engine.core.framework.AspectActivity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.logic.BehaviorLogic;

public abstract class BehaviorActivity extends AspectActivity {

	public BehaviorActivity(EntitySystem system) {
		super(system, new Aspect(TypeManager.getType(BehaviorLogic.class)));
	}

}
