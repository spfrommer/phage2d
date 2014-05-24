package engine.inputs.keyboard.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import engine.inputs.keyboard.Key;

public class HTMLKeyConfigLoader {
	public static Set<Key> s_parseKeys(InputStream inputStream) throws IOException {
		StringBuilder sb = new StringBuilder();

		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		String html = sb.toString();
		
		Set<Key> keys = new HashSet<Key>();
		
		Document doc = Jsoup.parse(html);
		Elements rows = doc.getElementsByTag("tr");//Get each row
		for (Element row : rows) {
			keys.add(s_parseHTMLKey(row));
		}
		return keys;
	}
	public static Key s_parseHTMLKey(Element row) {
		int id = 0;
		String name = "";
		char c = '\0';

		Elements elements = row.children();
		
		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if (e.tagName().equals("td")) {
				//first is id(value), then name, then description
				if (i == 0) {
					//System.out.println("Row 1: " + e.text());
					id = Integer.parseInt(e.text());
				} else if (i == 1) {//Name
					//System.out.println("Row 2: " + e.text());
					name = e.text();
				} else if (i == 2) {//Description
					//System.out.println("Row 3: " + e.text());
					String desc = e.text();
					if (desc.length() == 1) {//The char
						c = desc.charAt(0);
					}
				}
			}
		}
		if (c == '\0' && name.length() == 1) {//The name is also the char
			c = name.charAt(0);
		}
		
		return new Key(id, c, name);
	}
	
	public static void main(String[] args) throws Exception {
		InputStream stream = HTMLKeyConfigLoader.class.getResourceAsStream("/input/key_table_html.html");
		Set<Key> keys = s_parseKeys(stream);
		for (Key key : keys) {
			System.out.println(key);
		}
	}
}
