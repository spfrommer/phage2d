package examples.flipflop.level;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class LevelReader {
	private static final String READ_DIRECTORY = "/levels/";

	public static boolean levelExists(String levelName) {
		URL u = LevelReader.class.getResource(READ_DIRECTORY + levelName);
		return (u != null);
	}

	public static String read(String levelName) {
		InputStream in = null;
		in = LevelReader.class.getResourceAsStream(READ_DIRECTORY + levelName);

		Scanner scanner = new Scanner(in);
		String string = scanner.useDelimiter("\\A").next();
		scanner.close();
		return string;
	}
}
