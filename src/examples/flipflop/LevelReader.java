package examples.flipflop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LevelReader {
	private static final String READ_DIRECTORY = "/Users/samuel/Desktop/levels/";

	public static boolean levelExists(String levelName) {
		File file = new File(READ_DIRECTORY + levelName);
		return file.exists();
	}

	public static String read(String levelName) {
		File file = new File(READ_DIRECTORY + levelName);
		try {
			String string = new Scanner(file).useDelimiter("\\A").next();
			return string;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
