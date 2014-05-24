package engine.core.implementation.network.base.encoding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Encodes a DependentComponent with its class name.
 */
public class BasicDependentEncoder implements DependentEncoder {
	private Class<?> m_encodingClass;

	public BasicDependentEncoder(Class<?> encodingClass) {
		m_encodingClass = encodingClass;
	}

	@Override
	public Element encode(Document doc) {
		Element element = doc.createElement(m_encodingClass.getName());
		return element;
	}

}
