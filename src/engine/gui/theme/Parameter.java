package engine.gui.theme;

import engine.gui.theme.xml.XMLParsingUtils;

import java.io.File;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Parameter extends ThemeItem {
	private Object m_value;
	
	public Parameter() {}
	public Parameter(Parameter p) {
		super(p);
		setValue(p.getValue());
	}
	
	public void setValue(Object o) { m_value = o; }
	public Object getValue() { return m_value; }
	
	public String getItemType() {
		return "parameter";
	}

	@Override
	public void load(Theme root, ThemeItem parent, Element xmlElement, File file) {
		setName(xmlElement.getAttribute("name"));
		Element value = null;
		for (int i = 0; i < xmlElement.getChildNodes().getLength(); i++) {
			Node child = xmlElement.getChildNodes().item(i);
			if (child instanceof Element)  {
				value = (Element) child;
				break;
			}
		}
		//System.out.println(value.getTextContent());
		setValue(XMLParsingUtils.s_parseObject(value.getTagName(), value.getTextContent(), root));
		//System.out.println("param: " + getName() + " set to: " + getValue());
	}
	@Override
	public void loadAttribute(String attrName, String value) {
		
	}
	
	@Override
	public ThemeItem copy() {
		return new Parameter(this);
	}
}
