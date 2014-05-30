package engine.core.implementation.behavior.base.leaf.action.executor;

import engine.core.framework.Entity;
import engine.core.framework.component.LogicComponent;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;
import engine.core.implementation.behavior.base.leaf.action.ActionLeaf;

/**
 * A generic ActionLeaf that executes a LogicComponent that implements the ActionExecutable interface.
 * 
 * @param <T>
 *            the type of LogicComponent it should load and execute
 */
public class ActionExecutorLeaf<T extends LogicComponent & ActionExecutable> extends ActionLeaf {
	private Class<T> m_type;
	private T m_logic;

	@SuppressWarnings("unchecked")
	@Override
	public boolean load(Entity entity) {
		try {
			m_logic = (T) entity.getComponent(TypeManager.getType(m_type));
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@Override
	public Node copy() {
		return new ActionExecutorLeaf<T>();
	}

	@Override
	public ExecutionState update(int ticks) {
		return m_logic.update(ticks);
	}

}
