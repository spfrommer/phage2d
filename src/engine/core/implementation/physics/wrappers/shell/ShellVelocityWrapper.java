package engine.core.implementation.physics.wrappers.shell;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsShellData;
import engine.core.implementation.physics.wrappers.VelocityWrapper;

/**
 * Provides the velocity as defined by PhysicsShellData.
 * 
 * @eng.dependencies PhysicsShellData
 */
public class ShellVelocityWrapper extends VelocityWrapper {
	private PhysicsShellData m_shell;

	public ShellVelocityWrapper() {
		super(new Aspect(TypeManager.getType(PhysicsShellData.class)));
	}

	public ShellVelocityWrapper(Entity parent) {
		super(parent, new Aspect(TypeManager.getType(PhysicsShellData.class)));
	}

	@Override
	public Vector getVelocity() {
		return m_shell.velocity;
	}

	@Override
	public void loadDependencies() {
		m_shell = (PhysicsShellData) this.loadDependency(TypeManager.getType(PhysicsShellData.class));
	}

	@Override
	public Component copy() {
		return new ShellVelocityWrapper();
	}
}
