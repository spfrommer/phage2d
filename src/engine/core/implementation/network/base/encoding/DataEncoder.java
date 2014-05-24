package engine.core.implementation.network.base.encoding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import engine.core.framework.component.Component;

/**
 * Defines how to encode a data component
 */
public interface DataEncoder {
	/**
	 * Returns the XML Element that represents this DataComponent.
	 * 
	 * <fullpackage.component.class> <param1> val1 </param1> <param2> val2 </param2> </fullpackage.component.class>
	 * 
	 * @param component
	 *            the org.w3c.dom.Element that has a code for building the DataComponent
	 * @param doc
	 *            the Document needed to create a new Element
	 * 
	 * @return
	 */
	public Element encode(Component component, Document doc);
}
