package engine.gui;

import java.io.File;

import org.w3c.dom.Element;

import engine.gui.theme.Theme;
import engine.gui.theme.ThemeItem;

public class FontDef extends ThemeItem {

	@Override
	public String getItemType() {
		return "fontDef";
	}

	@Override
	public void load(Theme root, ThemeItem parent, Element xmlElement, File file) {
		
	}

	@Override
	public void loadAttribute(String attrName, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ThemeItem copy() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
