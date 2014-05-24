package engine.core.implementation.physics.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import engine.core.framework.component.Component;
import engine.core.implementation.network.base.encoding.DataEncoder;

/**
 * Encodes a PhysicsData into a PhysicsShellData.
 */
public class PhysicsToShellEncoder implements DataEncoder {
	@Override
	public Element encode(Component component, Document doc) {
		PhysicsData physicsComponent = (PhysicsData) component;

		Element shellElement = doc.createElement(PhysicsShellData.class.getName());

		Element positionxElement = doc.createElement("positionx");
		positionxElement.appendChild(doc.createTextNode("" + physicsComponent.getPosition().getX()));
		shellElement.appendChild(positionxElement);

		Element positionyElement = doc.createElement("positiony");
		positionyElement.appendChild(doc.createTextNode("" + physicsComponent.getPosition().getY()));
		shellElement.appendChild(positionyElement);

		Element rotationElement = doc.createElement("rotation");
		rotationElement.appendChild(doc.createTextNode("" + physicsComponent.getRotation()));
		shellElement.appendChild(rotationElement);

		Element centerxElement = doc.createElement("centerx");
		centerxElement.appendChild(doc.createTextNode("" + physicsComponent.getCenter().getX()));
		shellElement.appendChild(centerxElement);

		Element centeryElement = doc.createElement("centery");
		centeryElement.appendChild(doc.createTextNode("" + physicsComponent.getCenter().getY()));
		shellElement.appendChild(centeryElement);

		Element velocityxElement = doc.createElement("velocityx");
		velocityxElement.appendChild(doc.createTextNode("" + physicsComponent.getVelocity().getX()));
		shellElement.appendChild(velocityxElement);

		Element velocityyElement = doc.createElement("velocityy");
		velocityyElement.appendChild(doc.createTextNode("" + physicsComponent.getVelocity().getY()));
		shellElement.appendChild(velocityyElement);

		return shellElement;
	}
}
