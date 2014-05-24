package engine.core.implementation.physics.wrappers.shell;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsShellData;
import engine.core.implementation.physics.wrappers.CenterWrapper;

/**
 * Provides the center as defined by PhysicsShellData.
 * 
 * @eng.dependencies PhysicsShellData
 */
public class ShellCenterWrapper extends CenterWrapper {
	private PhysicsShellData m_shell;

	public ShellCenterWrapper() {
		super(new Aspect(TypeManager.getType(PhysicsShellData.class)));
	}

	public ShellCenterWrapper(Entity parent) {
		super(parent, new Aspect(TypeManager.getType(PhysicsShellData.class)));
	}

	@Override
	public Vector getCenter() {
		return m_shell.center;
	}

	@Override
	public void loadDependencies() {
		m_shell = (PhysicsShellData) this.loadDependency(TypeManager.getType(PhysicsShellData.class));
	}

	@Override
	public Component copy() {
		return new ShellCenterWrapper();
	}
}
