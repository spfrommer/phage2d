package engine.core.implementation.network.base.decoding;

import engine.core.framework.component.DataComponent;

public interface DataDecoder {
	public void decode(DataComponent component, String[] data);
}
