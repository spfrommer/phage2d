package engine.gui.theme.xml;

import engine.gui.theme.Area;
import engine.gui.theme.ImagesFile;
import engine.gui.theme.Parameter;
import engine.gui.theme.Select;
import engine.gui.theme.Theme;
import engine.gui.theme.ThemeItem;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLLoader {
	private static Logger logger = LoggerFactory.getLogger(XMLLoader.class);
	
	public static Theme s_load(File file) throws ParserConfigurationException, SAXException, IOException {
		logger.debug("Loading theme from file: " + file.getAbsolutePath());
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		
		Element root = doc.getDocumentElement();
		
		Theme theme = new Theme();
		
		s_parse(theme, theme, root, file);
		
		logger.debug("Finished loading theme...");
		
		return theme;
	}
	public static void s_parseChildren(Theme root, ThemeItem parent, Element element, File file) throws ParserConfigurationException, SAXException, IOException {
		NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node instanceof Element) {
				Element e = (Element) node;
				ThemeItem ti = s_parse(root, parent, e, file);
				if (ti != null) parent.add(ti);
			}
		}
	}
	public static ThemeItem s_parse(Theme root, ThemeItem parent, Element element, File file) throws ParserConfigurationException, SAXException, IOException {
		ThemeItem item = null;
		if (element.getTagName().equals("themes")) {
			s_parseChildren(root, parent, element, file);
		} else if (element.getTagName().equals("theme")) {
			Theme t = null;
			if (element.getAttribute("ref").equals("")) {
				t = new Theme();
			} else {
				String ref = element.getAttribute("ref");
				Theme refItem = (Theme) root.getItem(ref, "theme");
				if (refItem != null) t = (Theme) refItem.copy();
				else t = new Theme();
			}
			t.load(root, parent, element, file);
			//TODO: figure out weird button-missing problem when parent.add goes before load
			//could it be that the add is replacing another item with a blank name
			parent.add(t);
			s_parseChildren(root, t, element, file);
			item = t;
		} else if (element.getTagName().equals("param")) {
			Parameter parameter = new Parameter();
			parameter.load(root, parent, element, file);
			parent.add(parameter);
			item = parameter;
		} else if (element.getTagName().equals("images")) {
			ImagesFile images = new ImagesFile();
			images.load(root, parent, element, file);
			parent.add(images);
			s_parseChildren(root, images, element, file);
			item = images;
		} else if (element.getTagName().equals("area")) {
			Area area = new Area();
			area.load(root, parent, element, file);
			parent.add(area);
			s_loadAttributes(element, area);
			s_parseChildren(root, area, element, file);
			item = area;
		} else if (element.getTagName().equals("select")) {
			Select select = new Select();
			select.load(root, parent, element, file);
			parent.add(select);
		} else if (element.getTagName().equals("include")) {
			String fileName = element.getAttribute("filename");
			File include = new File(file.getParentFile(), fileName);
			parent.add(s_load(include));
		} else if (element.getTagName().equals("composed")) {
			logger.warn("Composed not implemented yet!!");
		} else if (element.getTagName().equals("grid")) {
			logger.warn("Grid not implemented yet!!");
		} else if (element.getTagName().equals("alias")) {
		
			ThemeItem ref = root.getItem(element.getAttribute("ref"));
			if (ref == null) logger.error("alias \"" + element.getAttribute("ref") + "\" was not found");
			else {
				ThemeItem alias = ref.copy();
				s_loadAttributes(element, alias);
				item = alias;
			}
		}
		//System.out.println("Parsed to: " + item);
		return item;
	}
	//---Helper functions---
	private static void s_loadAttributes(Element e, ThemeItem item) {
		NamedNodeMap attributes = e.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Attr attribute = (Attr) attributes.item(i);
			if (!attribute.getName().equals("ref")) {
				item.loadAttribute(attribute.getName(), attribute.getValue());
			}
		}
	}
	
	public static void main(String[] args) throws URISyntaxException, ParserConfigurationException, SAXException, IOException {
		File file = new File(XMLLoader.class.getResource("/themes/basic/chutzpah.xml").toURI());
		Theme theme = s_load(file);
	}
}
