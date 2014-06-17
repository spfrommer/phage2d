package engine.core.implementation.network.base.decoding;

import engine.core.framework.component.DataComponent;

/**
 * A DataDecoder that doesn't do anything.
 */
public class BlankDataDecoder implements DataDecoder {
	@Override
	public void decode(DataComponent component, String[] data) {

	}
}
