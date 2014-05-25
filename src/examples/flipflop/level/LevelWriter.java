package examples.flipflop.level;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dyn4j.geometry.Rectangle;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.data.PhysicsData;
import examples.flipflop.PortalManager;

public class LevelWriter {
	public static void print(Level level) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("level");
		doc.appendChild(rootElement);

		for (Entity entity : level.getBalls()) {
			Element ball = doc.createElement("ball");
			rootElement.appendChild(ball);

			addPositionAttributes(entity, ball, doc);
		}

		for (Entity entity : level.getPortals(new PortalManager(
				new EntitySystem()))) {
			Element portal = doc.createElement("portal");
			rootElement.appendChild(portal);
			addPositionAttributes(entity, portal, doc);
		}

		for (Entity entity : level.getPlatforms()) {
			Element platform = doc.createElement("platform");
			rootElement.appendChild(platform);
			addPositionAttributes(entity, platform, doc);
			addDimensionAttributes(entity, platform, doc);
		}
		try {
			printDocument(doc, System.out);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	private static void printDocument(Document doc, OutputStream out)
			throws IOException, TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(doc), new StreamResult(
				new OutputStreamWriter(out, "UTF-8")));
	}

	private static void addPositionAttributes(Entity entity, Element element,
			Document doc) {
		PhysicsData physics = (PhysicsData) entity.getComponent(TypeManager
				.getType(PhysicsData.class));

		Attr xattr = doc.createAttribute("x");
		xattr.setValue("" + physics.getPosition().getX());
		element.setAttributeNode(xattr);

		Attr yattr = doc.createAttribute("y");
		yattr.setValue("" + physics.getPosition().getY());
		element.setAttributeNode(yattr);
	}

	private static void addDimensionAttributes(Entity entity, Element element,
			Document doc) {
		PhysicsData physics = (PhysicsData) entity.getComponent(TypeManager
				.getType(PhysicsData.class));
		Attr xattr = doc.createAttribute("width");
		xattr.setValue("" + ((Rectangle) physics.getConvex()).getWidth());
		element.setAttributeNode(xattr);

		Attr yattr = doc.createAttribute("height");
		yattr.setValue("" + ((Rectangle) physics.getConvex()).getHeight());
		element.setAttributeNode(yattr);
	}
}
