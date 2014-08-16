package engine.core.implementation.physics.wrappers;

import utils.physics.Vector;
import engine.core.framework.Aspect;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsShellData;

/**
 * A transform wrapper for PhysicsShellData.
 * 
 * @eng.dependencies PhysicsShellData
 */
public class ShellTransformWrapper extends TransformWrapper {
	private PhysicsShellData m_shell;

	public ShellTransformWrapper() {
		super(new Aspect(TypeManager.typeOf(PhysicsShellData.class)));
	}

	@Override
	public Vector getPosition() {
		return m_shell.position;
	}

	@Override
	public void setPosition(Vector position) {
		m_shell.position = position;
	}

	@Override
	public Vector getCenter() {
		return m_shell.center;
	}

	@Override
	public void setCenter(Vector center) {
		m_shell.center = center;
	}

	@Override
	public double getRotation() {
		return m_shell.rotation;
	}

	@Override
	public void setRotation(double rotation) {
		m_shell.rotation = rotation;

	}

	@Override
	public void loadDependencies() {
		m_shell = (PhysicsShellData) this.loadDependency(TypeManager.typeOf(PhysicsShellData.class));
	}

	@Override
	public Component copy() {
		return new ShellTransformWrapper();
	}
}