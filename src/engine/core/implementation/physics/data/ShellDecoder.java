package engine.core.implementation.physics.data;

import utils.physics.Vector;
import engine.core.framework.component.DataComponent;
import engine.core.implementation.network.base.decoding.DataDecoder;

/**
 * Decodes PhysicsShellData.
 */
public class ShellDecoder implements DataDecoder {
	@Override
	public void decode(DataComponent component, String[] data) {
		PhysicsShellData shell = (PhysicsShellData) component;
		shell.position = new Vector(Double.parseDouble(data[0]), Double.parseDouble(data[1]));
		shell.rotation = Double.parseDouble(data[2]);
		shell.center = new Vector(Double.parseDouble(data[3]), Double.parseDouble(data[4]));
	}
}
