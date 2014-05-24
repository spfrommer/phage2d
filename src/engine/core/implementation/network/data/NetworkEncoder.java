package engine.core.implementation.network.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import engine.core.framework.component.Component;
import engine.core.implementation.network.base.encoding.DataEncoder;

/**
 * Encodes the NetworkData component.
 */
public class NetworkEncoder implements DataEncoder {
	private boolean m_sync;

	public NetworkEncoder(boolean sync) {
		m_sync = sync;
	}

	@Override
	public Element encode(Component component, Document doc) {
		NetworkData networkComponent = (NetworkData) component;
		Element networkElement = doc.createElement(NetworkData.class.getName());

		Element id = doc.createElement("id");
		id.appendChild(doc.createTextNode("" + networkComponent.id));
		networkElement.appendChild(id);

		Element sync = doc.createElement("sync");
		sync.appendChild(doc.createTextNode("" + (m_sync ? "true" : "false")));
		networkElement.appendChild(sync);

		return networkElement;
	}

}
