package engine.graphics.lwjgl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.lwjgl.LWJGLUtil;

import utils.IOUtils;

/**
 * Code copied from OpenClassic project by Steveice10 and modified to avoid using Apache commons.
 */
public class LWJGLNatives {
	public static void load(File dir) {
		if (LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_WINDOWS) {
			extract(dir, "lwjgl.dll", "86");
			extract(dir, "lwjgl64.dll", "64");
			extract(dir, "OpenAL32.dll", "86");
			extract(dir, "OpenAL64.dll", "64");
			extract(dir, "jinput-raw.dll", "86");
			extract(dir, "jinput-raw_64.dll", "64");
			extract(dir, "jinput-dx8.dll", "86");
			extract(dir, "jinput-dx8_64.dll", "64");
		} else if (LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_LINUX) {
			extract(dir, "liblwjgl.so", "86");
			extract(dir, "liblwjgl64.so", "64");
			extract(dir, "libopenal.so", "86");
			extract(dir, "libopenal64.so", "64");
			extract(dir, "libjinput-linux.so", "86");
			extract(dir, "libjinput-linux64.so", "64");
		} else if (LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_MACOSX) {
			extract(dir, "liblwjgl.jnilib", "both");
			extract(dir, "openal.dylib", "both");
			extract(dir, "libjinput-osx.jnilib", "both");
		} else {
			throw new RuntimeException("Could not find platform");
		}

		System.setProperty("java.library.path", dir.getPath());
		System.setProperty("org.lwjgl.librarypath", dir.getPath());
		System.setProperty("net.java.games.input.librarypath", dir.getPath());
	}

	private static void extract(File dir, String lib, String arch) {
		if (arch.equals("both") || System.getProperty("os.arch").contains(arch)) {
			File file = new File(dir, lib);
			boolean writing = false;
			System.out.println("Checking for " + lib + " at " + file.getPath());
			try {
				if (file.exists()) {
					InputStream in = LWJGLNatives.class.getResourceAsStream("/" + lib);
					InputStream fin = new FileInputStream(file);
					try {
						if (IOUtils.contentEquals(in, fin)) {
							System.out.println(lib + " is up to date at " + file.getPath());
							return;
						}
					} finally {
						in.close();
						fin.close();
					}
				} else {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}

				writing = true;
				InputStream in = LWJGLNatives.class.getResourceAsStream("/" + lib);
				System.out.println("Writing " + lib + " to " + file.getPath());
				FileOutputStream out = new FileOutputStream(file);
				IOUtils.copy(in, out);
				in.close();
				out.close();
			} catch (Exception e) {
				if (writing) {
					System.err.println("Failed to write library " + lib + " to " + file.getPath());
				} else {
					System.err.println("Failed to check for library " + lib + " at " + file.getPath());
				}

				e.printStackTrace();
			}
		}
	}

}
