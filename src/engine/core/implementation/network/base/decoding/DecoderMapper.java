package engine.core.implementation.network.base.decoding;

import java.util.HashMap;
import java.util.Map;

import engine.core.framework.component.DataComponent;

/**
 * Maps DataComponent classes with the appropriate decoder to use.
 */
public class DecoderMapper {
	private Map<Class<? extends DataComponent>, DataDecoder> m_decoders;

	{
		m_decoders = new HashMap<Class<? extends DataComponent>, DataDecoder>();
	}

	public DecoderMapper() {

	}

	public void addMapping(Class<? extends DataComponent> comp, DataDecoder decoder) {
		m_decoders.put(comp, decoder);
	}

	public void removeMapping(Class<? extends DataComponent> comp) {
		m_decoders.remove(comp);
	}

	public DataDecoder getDecoder(Class<? extends DataComponent> comp) {
		if (m_decoders.containsKey(comp)) {
			return m_decoders.get(comp);
		} else {
			return new BasicDataDecoder();
		}
	}
}
