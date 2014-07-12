package migrate.input;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLKeyConfigLoader {
	public static void s_parseConfig(InputStream inputStream, Keyboard keyboard) throws Exception {
		//Get the DOM Builder Factory
		DocumentBuilderFactory factory = 
				DocumentBuilderFactory.newInstance();

		//Get the DOM Builder
		DocumentBuilder builder = factory.newDocumentBuilder();

		//Load and Parse the XML document
		//document contains the complete XML as a Tree.
		Document document = 
				builder.parse(inputStream);

		NodeList nodes = document.getDocumentElement().getElementsByTagName("keys");
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node instanceof Element) {
				s_parseKeyConfig((Element) node, keyboard);
			}
		}
	}
	public static void s_parseKeyConfig(Element e, Keyboard k) {

		//Iterating through the nodes and extracting the data.
		NodeList nodeList = e.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			//We have encountered an <employee> tag.
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				Element element = (Element) node;
				if (element.getTagName().equals("key")) {
					s_parseKey(element, k);
				}
			}
		}
	}
	public static void s_parseKey(Element element, Keyboard k) {
		int id = Integer.parseInt(element.getAttribute("id"));
		String charString = element.getAttribute("char");
		char c = '\0';
		if (charString.length() > 0) c = charString.charAt(0);
		
		String name = element.getAttribute("name");
		if (name.equals("")) name = Character.toString(c);

		boolean shiftKey = Boolean.parseBoolean(element.getAttribute("isShift"));
		boolean modKey = Boolean.parseBoolean(element.getAttribute("isMod"));
		
		Key key = new Key(c, name, id);
		k.addKey(key);
		if (shiftKey) k.addShiftKey(key);
		if (modKey) k.addModKey(key);
	}
}
