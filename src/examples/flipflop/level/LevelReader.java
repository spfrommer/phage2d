package examples.flipflop.level;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class LevelReader {
	private static final String READ_DIRECTORY = "/levels/";

	public static boolean levelExists(String levelName) {
		URL u = LevelReader.class.getResource(READ_DIRECTORY + levelName);
		return (u != null);
	}

	public static String read(String levelName) {
		File file = null;
		try {
			file = new File(LevelReader.class.getResource(
					READ_DIRECTORY + levelName).toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		try {
			String string = new Scanner(file).useDelimiter("\\A").next();
			return string;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
