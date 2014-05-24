package engine.gui.theme.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import engine.gui.border.EmptyBorder;
import engine.gui.theme.Inset;
import engine.gui.theme.Theme;

public class XMLParsingUtils {
	private static Logger logger = LoggerFactory.getLogger(XMLParsingUtils.class);
	
	public static Inset s_parseInset(String inset) {
		int t = 0, l = 0, b = 0, r = 0;
		
		String[] parts = inset.split(",");
		if (parts.length == 1) {
			l = Integer.parseInt(parts[0]);
			r = l; t = l; b = l;
		} else if (parts.length == 2) {
			l = Integer.parseInt(parts[0]);
			r = l;
			t = Integer.parseInt(parts[1]);
			b = t;
		} else if (parts.length == 4) {
			t = Integer.parseInt(parts[0]);
			l = Integer.parseInt(parts[1]);
			b = Integer.parseInt(parts[2]);
			r = Integer.parseInt(parts[3]);
		}
		return new Inset(l, r, t, b);
	}
	public static Object s_parseObject(String name, String input, Theme root) {
		Object val = null;
		
		if (name.equals("int")) {
			if (input.matches("-?\\d+")) {
				val = Integer.parseInt(input);
			}
		} else if (name.equals("string")) {
			val = name;
		} else if (name.equals("image")) {
			val = root.getItem(input);
			/*if (val != null) System.out.println("Found image: " + ((ThemeItem) val).getName() + " type: " + 
						((ThemeItem) val).getItemType() + " for: " + value);
			else*/
			if (val == null) logger.error("No image for: " + input);
		} else if (name.equals("border")) {
			logger.debug("input: {}", input);
			EmptyBorder border = new EmptyBorder(input);
			val = border;
		} else if (name.equals("bool")) {
			val = Boolean.parseBoolean(input);
		}
		return val;
	}
}
