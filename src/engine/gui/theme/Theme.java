package engine.gui.theme;

import java.io.File;

import org.w3c.dom.Element;

public class Theme extends ThemeItem {
	/*
	 * No longer used
	 * 
	 * Will retrieve an item based on its path
	public ThemeItem getPath(String path) {
		String[] parts = path.split(".");
		ThemeItem current = this;
		for (String item : parts) {
			current = current.get(item);
		}
		return current;
	}*/
	
	public Theme() {}
	public Theme(Theme theme) {
		super(theme);
	}
	
	public String getItemType() {
		return "theme";
	}

	@Override
	public void load(Theme root, ThemeItem parent, Element xmlElement, File file) {
		setName(xmlElement.getAttribute("name"));
	}
	@Override
	public void loadAttribute(String name, String value) {
		
	}
	
	@Override
	public ThemeItem copy() {
		return new Theme(this);
	}
}
