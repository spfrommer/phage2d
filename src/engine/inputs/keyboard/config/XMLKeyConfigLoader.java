package engine.inputs.keyboard.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import engine.inputs.keyboard.Key;
import engine.inputs.keyboard.Key.MultiKey;
import engine.inputs.keyboard.Key.ShiftKeyModifier;

public class XMLKeyConfigLoader {
	public static Set<Key> s_parseKeys(InputStream inputStream) throws SAXException, IOException, ParserConfigurationException {
		//Get the DOM Builder Factory
		DocumentBuilderFactory factory = 
				DocumentBuilderFactory.newInstance();

		//Get the DOM Builder
		DocumentBuilder builder = factory.newDocumentBuilder();

		//Load and Parse the XML document
		//document contains the complete XML as a Tree.
		Document document = 
				builder.parse(inputStream);

		Set<Key> keys = new HashSet<Key>();
		
		NodeList nodes = document.getDocumentElement().getElementsByTagName("keys");
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node instanceof Element) {
				keys.addAll(s_parseKeys((Element) node));
			}
		}
		
		return keys;
	}
	public static Set<Key> s_parseKeys(Element e) {
		Set<Key> keys = new HashSet<Key>();

		//Iterating through the nodes and extracting the data.
		NodeList nodeList = e.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			//We have encountered an <employee> tag.
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				Element element = (Element) node;
				if (element.getTagName().equals("key")) {
					Key key = s_parseKey(element);
					s_checkSpecialKey(key);
					keys.add(key);
				} else if (element.getTagName().equals("multikey")) {
					Set<Key> subKeys = s_parseKeys(element);
					Key key = s_parseKey(element);
					MultiKey multi = new MultiKey();
					multi.inherit(key);
					multi.addAll(subKeys);
					s_checkSpecialKey(multi);
					keys.add(multi);
					//Add subkeys to make sure they get polled
					keys.addAll(subKeys);
				}
			}
		}
		return keys;
	}
	//TODO: Move to config
	private static void s_checkSpecialKey(Key key) {
		if (key.getName().equals("SHIFT")) {
			key.addModifier(new ShiftKeyModifier());
		}
	}
	public static Key s_parseKey(Element element) {
		int id = Integer.parseInt(element.getAttribute("id"));
		String charString = element.getAttribute("char");
		char c = '\0';
		if (charString.length() > 0) c = charString.charAt(0);
		
		String name = element.getAttribute("name");
		if (name.equals("")) name = Character.toString(c);

		return new Key(id, c, name);
	}
	
	public static void main(String[] args) throws Exception {
		InputStream stream = XMLKeyConfigLoader.class.getResourceAsStream("/input/keys_lwjgl.xml");
		Set<Key> keys = s_parseKeys(stream);
		for (Key key : keys) {
			System.out.println(key);
		}
	}
}
