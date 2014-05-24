package engine.gui.theme;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.w3c.dom.Element;

public abstract class ThemeItem {
	private String m_name;
	private ThemeItem m_parent;
	private ArrayList<ThemeItem> m_children = new ArrayList<ThemeItem>();
	
	public ThemeItem() {}
	public ThemeItem(ThemeItem item) {
		setName(item.getName());
		setParent(item);
		m_children = new ArrayList<ThemeItem>(item.getChildren());
	}
	
	public void setName(String name) { m_name = name; }
	public void setParent(ThemeItem parent) { m_parent = parent; }
	public String getName() { if (m_name != null) return m_name; else return ""; }
	public ThemeItem getParent() { return m_parent; }
	public Collection<ThemeItem> getChildren() { return m_children; }
	
	public void add(ThemeItem child) {
		//Check if child with that name already exists
		ThemeItem existing  = get(child.getName());
		if (existing != null) remove(existing);

		m_children.add(child);
		//if (child.getName().equals("background")) System.out.println("Adding background" + child + " to " + getName());
		
		child.setParent(this);
	}
	public void remove(ThemeItem child) {
		m_children.remove(child);
		child.setParent(null);
	}
	
	public ThemeItem get(String name) {
		for (ThemeItem item : m_children) {
			if (item.getName().equals(name)) return item;
		}
		return null;
	}
	public ThemeItem get(String name, String type) {
		for (ThemeItem item : m_children) {
			if (item.getName().equals(name) && item.getItemType().equals(type)) return item;
		}
		return null;
	}
	public ThemeItem getItem(String name) {
		ThemeItem get = get(name);
		if (get != null) return get; 
		for (ThemeItem item : getChildren()) {
			ThemeItem result = item.getItem(name);
			if (result != null) return result;
		}
		return null;
	}
	public ThemeItem getItem(String name, String type) {
		ThemeItem get = get(name, type);
		if (get != null) return get;
		for (ThemeItem item : getChildren()) {
			ThemeItem result = item.getItem(name, type);
			if (result != null) return result;
		}
		return null;
	}
	
	public abstract String getItemType();	
	public abstract void load(Theme root, ThemeItem parent, Element xmlElement, File file);
	public abstract void loadAttribute(String attrName, String value);
	public abstract ThemeItem copy();
}
