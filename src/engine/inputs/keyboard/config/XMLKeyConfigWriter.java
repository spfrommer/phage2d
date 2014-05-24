package engine.inputs.keyboard.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Set;

import engine.inputs.keyboard.Key;

public class XMLKeyConfigWriter {
	public static void s_write(Set<Key> keys, OutputStream output) {
		PrintWriter writer = new PrintWriter(output);
		
		writer.println("<xml>");
		writer.println("<keys>");
		
		for (Key key : keys) {
			s_writeKey(key, writer);
		}
		
		writer.println("</keys>");
		writer.println("</xml>");
		
		writer.flush();
	}
	private static void s_writeKey(Key key, PrintWriter writer) {
		int id = key.getID();
		String name = key.getName();
		char c = key.getCharacter();
		
		String line;
		
		if (Character.toString(c).equals(name)) line = "<key id=\"" + id + "\" char=\"" + c + "\"/>";
		else if (c == '\0') line = "<key id=\"" + id + "\" name=\"" + name + "\"/>";
		else line = "<key id=\"" + id + "\" name=\"" + name + "\" char=\"" + c + "\"/>";
		
		writer.println(line);
	}
	
	public static void main(String[] args) throws URISyntaxException, IOException {
		InputStream stream = HTMLKeyConfigLoader.class.getResourceAsStream("/input/key_table_html.html");
		Set<Key> keys = HTMLKeyConfigLoader.s_parseKeys(stream);
		
		//TODO:Better resource locating in /resources without /bin
		File file = new File(System.getProperty("user.home") + "/programs/bitbucket/game_library_2d/Game Library/resources/input/keys.xml");
		
		file.createNewFile();
		
		FileOutputStream out = new FileOutputStream(file);
		s_write(keys, out);
	}
}
