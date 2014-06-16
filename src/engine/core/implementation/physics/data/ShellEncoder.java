package engine.core.implementation.physics.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import engine.core.framework.component.Component;
import engine.core.implementation.network.base.encoding.DataEncoder;

/**
 * Encodes a PhysicsShellData.
 */
public class ShellEncoder implements DataEncoder {
	@Override
	public Element encode(Component component, Document doc) {
		PhysicsShellData shellComponent = (PhysicsShellData) component;

		Element shellElement = doc.createElement(PhysicsShellData.class.getName());

		Element positionxElement = doc.createElement("positionx");
		positionxElement.appendChild(doc.createTextNode("" + shellComponent.position.getX()));
		shellElement.appendChild(positionxElement);

		Element positionyElement = doc.createElement("positiony");
		positionyElement.appendChild(doc.createTextNode("" + shellComponent.position.getY()));
		shellElement.appendChild(positionyElement);

		Element rotationElement = doc.createElement("rotation");
		rotationElement.appendChild(doc.createTextNode("" + shellComponent.rotation));
		shellElement.appendChild(rotationElement);

		Element centerxElement = doc.createElement("centerx");
		centerxElement.appendChild(doc.createTextNode("" + shellComponent.center.getX()));
		shellElement.appendChild(centerxElement);

		Element centeryElement = doc.createElement("centery");
		centeryElement.appendChild(doc.createTextNode("" + shellComponent.center.getY()));
		shellElement.appendChild(centeryElement);

		return shellElement;
	}
}
