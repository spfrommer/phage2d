package engine.core.implementation.physics.wrappers.shell;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsShellData;
import engine.core.implementation.physics.wrappers.RotationWrapper;

/**
 * Provides the rotation as defined by PhysicsShellData.
 * 
 * @eng.dependencies PhysicsShellData
 */
public class ShellRotationWrapper extends RotationWrapper {
	private PhysicsShellData m_shell;

	public ShellRotationWrapper() {
		super(new Aspect(TypeManager.getType(PhysicsShellData.class)));
	}

	public ShellRotationWrapper(Entity parent) {
		super(parent, new Aspect(TypeManager.getType(PhysicsShellData.class)));
	}

	@Override
	public double getRotation() {
		return m_shell.rotation;
	}

	@Override
	public void loadDependencies() {
		m_shell = (PhysicsShellData) this.loadDependency(TypeManager.getType(PhysicsShellData.class));
	}

	@Override
	public Component copy() {
		return new ShellRotationWrapper();
	}
}
