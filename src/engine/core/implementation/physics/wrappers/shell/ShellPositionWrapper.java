package engine.core.implementation.physics.wrappers.shell;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsShellData;
import engine.core.implementation.physics.wrappers.PositionWrapper;

/**
 * Provides the position as defined by PhysicsShellData.
 * 
 * @eng.dependencies PhysicsShellData
 */
public class ShellPositionWrapper extends PositionWrapper {
	private PhysicsShellData m_shell;

	public ShellPositionWrapper() {
		super(new Aspect(TypeManager.getType(PhysicsShellData.class)));
	}

	public ShellPositionWrapper(Entity parent) {
		super(parent, new Aspect(TypeManager.getType(PhysicsShellData.class)));
	}

	@Override
	public Vector getPosition() {
		return m_shell.position;
	}

	@Override
	public void loadDependencies() {
		m_shell = (PhysicsShellData) this.loadDependency(TypeManager.getType(PhysicsShellData.class));
	}

	@Override
	public Component copy() {
		return new ShellPositionWrapper();
	}
}
