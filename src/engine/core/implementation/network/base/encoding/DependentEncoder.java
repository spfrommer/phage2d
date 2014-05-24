package engine.core.implementation.network.base.encoding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Defines how to encode a DependentComponent. No Component parameter is needed because these dependencies are generated
 * dynamically when the Entity is reconstructed.
 */
public interface DependentEncoder {
	/**
	 * Returns the XML Element that represents this DependentComponent. No parameters are needed.
	 * 
	 * <fullpackage.component.class></fullpackage.component.class>
	 * 
	 * @param doc
	 *            the Document needed to create a new Element
	 * @param comp
	 *            the org.w3c.dom.Element that has a code for building the DependentComponent
	 * @return
	 */
	public Element encode(Document doc);
}
