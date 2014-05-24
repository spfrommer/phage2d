package engine.core.implementation.network.base.encoding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import engine.core.framework.component.Component;

public interface Encoder {
	/**
	 * Returns an Element constructed using the document
	 * 
	 * @param component
	 * @param doc
	 * @param element
	 * @return
	 */
	public Element encode(Component component, Document doc);
}
