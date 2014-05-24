package engine.gui.theme;

import engine.graphics.Renderer;
import engine.gui.AnimationState;
import engine.gui.condition.Condition;
import engine.gui.condition.Condition.ConditionParser;
import engine.gui.theme.xml.XMLLoader;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Select extends ImageItem {
	private static final Logger logger = LoggerFactory.getLogger(Select.class);
	
	private LinkedHashMap<Condition, ImageItem> m_map = new LinkedHashMap<Condition, ImageItem>();
	
	public Select() {}
	public Select(Select s) {
		m_map = new LinkedHashMap<Condition, ImageItem>(s.getMap());
	}
	
	public void addMap(Condition condition, ImageItem item) {
		m_map.put(condition, item);
	}
	public LinkedHashMap<Condition, ImageItem> getMap() { return m_map; }
	
	@Override
	public void render(Renderer r, AnimationState as, int x, int y) {
		for (Entry<Condition, ImageItem> entry : m_map.entrySet()) {
			if (entry.getKey().satisfies(as)) {
				entry.getValue().render(r, as, x, y);
				break;
			}
		}
	}

	@Override
	public void render(Renderer r, AnimationState as, int x, int y, int width,
			int height) {
		for (Entry<Condition, ImageItem> entry : m_map.entrySet()) {
			if (entry.getKey().satisfies(as)) {
				entry.getValue().render(r, as, x, y, width, height);
				break;
			}
		}
	}

	@Override
	public int getHeight() {
		return ((ImageItem) m_map.values().toArray()[0]).getHeight();
	}

	@Override
	public int getWidth() {
		return ((ImageItem) m_map.values().toArray()[0]).getWidth();
	}
	
	@Override
	public int getMinHeight() {
		return ((ImageItem) m_map.values().toArray()[0]).getMinHeight();
	}
	@Override
	public int getMinWidth() {
		return ((ImageItem) m_map.values().toArray()[0]).getMinWidth();
	}

	@Override
	public void tint(Color color) {
		for (Entry<Condition, ImageItem> entry : m_map.entrySet()) {
			ImageItem clone = (ImageItem) entry.getValue().copy();
			clone.tint(color);
			m_map.put(entry.getKey(), clone);
		}
	}

	@Override
	public void load(Theme root, ImagesFile images, Element element, File file) {
		setName(element.getAttribute("name"));
		NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			if (n instanceof Element) {
				Element e = (Element) n;
				ImageItem image;
				try {
					image = (ImageItem) XMLLoader.s_parse(root, images, e, file);
					String con = e.getAttribute("if");
					m_map.put(ConditionParser.s_parse(con), image);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		}
	}

	@Override
	public String getItemType() {
		return "select";
	}

	@Override
	public void loadAttribute(String attrName, String value) {
		if (attrName.equals("name")) setName(value);
	}

	@Override
	public String toString() {
		return getName() + " " + m_map;
	}
	
	@Override
	public ThemeItem copy() {
		return new Select(this);
	}

}
