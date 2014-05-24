package engine.core.implementation.network.base.decoding;

import engine.core.framework.component.DataComponent;

/**
 * Throws a RuntimeException when decode is called.
 */
public class ErrorDecoder implements DataDecoder {
	@Override
	public void decode(DataComponent component, String[] data) {
		throw new RuntimeException("Should not be decoding component: " + component);
	}
}
