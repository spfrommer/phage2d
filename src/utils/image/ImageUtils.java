package utils.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageUtils {
	private static HashMap<String, Integer> s_idMapper;
	private static HashMap<Integer, BufferedImage> s_imagePool;

	static {
		s_idMapper = new HashMap<String, Integer>();
		s_imagePool = new HashMap<Integer, BufferedImage>();
	}

	private ImageUtils() {
	}

	public static void initMapping(String file) {
		try {
			// File images = new File(ImageUtils.class.getResource("/images/" + file).toURI());
			// BufferedReader br = new BufferedReader(new FileReader(images));
			BufferedReader br = new BufferedReader(new InputStreamReader(
					ImageUtils.class.getResourceAsStream("/images/" + file)));
			int idCounter = 0;

			String line;
			while ((line = br.readLine()) != null) {
				s_idMapper.put(line, idCounter);
				s_imagePool.put(idCounter, readImage("/images/" + line));
				idCounter++;
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Image scaleImage(Image image, double width, double height) {
		return image.getScaledInstance((int) width, (int) height, Image.SCALE_SMOOTH);
	}

	public static Image scaleToHeight(Image image, double height) {
		int scaledWidth = (int) ((height / image.getHeight(null)) * image.getWidth(null));
		int scaledHeight = (int) height;
		Image scaledImage = scaleImage(image, scaledWidth, scaledHeight);
		return scaledImage;
	}

	public static int getID(String name) {
		return s_idMapper.get(name);
	}

	public static BufferedImage getImage(int id) {
		if (!s_imagePool.containsKey(id)) {
			System.out.println("No Image for ID: " + id);
			return null;
		}
		return s_imagePool.get(id);
	}

	public static BufferedImage cloneImage(BufferedImage source) {
		if (source == null)
			return null;
		BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = b.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return b;
	}

	public static BufferedImage readImage(String name) {
		BufferedImage image = null;
		try {
			// image = ImageIO.read(new File(ImageUtils.class.getResource(name).toURI()));
			image = ImageIO.read(ImageUtils.class.getResourceAsStream(name));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return image;
	}
}
