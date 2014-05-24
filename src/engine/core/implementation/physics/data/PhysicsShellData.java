package engine.core.implementation.physics.data;

import utils.physics.Vector;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;

/**
 * Only stores a basic shell of physics data needed for rendering on the client side or server-side entities that don't
 * interact with their world. Contains position, rotation, center, and velocity. These values are NOT updated by the
 * PhysicsActivity.
 */
public class PhysicsShellData extends DataComponent {
	public Vector position;
	public double rotation;
	public Vector center;

	// for interpolation
	public Vector velocity;

	public PhysicsShellData() {
		super();
	}

	public PhysicsShellData(Entity parent) {
		super(parent);
	}

	@Override
	public Component copy() {
		PhysicsShellData shell = new PhysicsShellData();
		shell.position = new Vector(this.position);
		shell.rotation = this.rotation;
		shell.center = new Vector(this.center);
		return shell;
	}
}
