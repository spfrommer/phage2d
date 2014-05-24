package engine.core.implementation.network.base.decoding;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;

/**
 * Decodes an entity from XML. Mainly used by ClientLogic to decode Entities sent from the ServerLogic.
 */
public class EntityDecoder {
	/**
	 * Extracts an entity from the given XML.
	 * 
	 * @param document
	 * @param mapper
	 * @return
	 */
	public static Entity decode(String document, DecoderMapper mapper) {
		Document doc = null;
		try {
			doc = stringToDoc(document);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Entity entity = new Entity();

		Node rootNode = doc.getDocumentElement();
		NodeList components = rootNode.getChildNodes();
		for (int j = 0; j < components.getLength(); j++) {
			Node node = components.item(j);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				Component comp = elementToComponent(element);
				comp.setEntity(entity);
				if (comp instanceof DataComponent) {
					List<String> data = getData(element);
					mapper.getDecoder(((DataComponent) comp).getClass()).decode((DataComponent) comp,
							data.toArray(new String[data.size()]));
				}
				entity.addComponent(comp);
			}
		}

		return entity;
	}

	private static List<String> getData(Element element) {
		NodeList dataList = element.getChildNodes();
		ArrayList<String> data = new ArrayList<String>();
		for (int i = 0; i < dataList.getLength(); i++) {
			Node n = dataList.item(i);
			if (!n.getNodeName().equals("#text")) {
				data.add(n.getTextContent());
			}
		}
		return data;
	}

	private static Component elementToComponent(Element node) {
		Component comp = null;
		try {
			comp = (Component) Class.forName(node.getNodeName()).getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comp;
	}

	private static Document stringToDoc(String xmlSource) throws SAXException, ParserConfigurationException,
			IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(xmlSource)));
	}
}
