package utils.image;

import java.awt.Color;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;

public class ImageShapeUtils {
	public static Polygon getPolygonForArea(Area area) {
		Polygon polygon = new Polygon();
		PathIterator path = area.getPathIterator(null);

		while (!path.isDone()) {
			double[] point = new double[2];
			if (path.currentSegment(point) != PathIterator.SEG_CLOSE)
				polygon.addPoint((int) point[0], (int) point[1]);
		}

		return polygon;
	}

	public static Area getArea(Image image) {
		Area area = new Area();
		Rectangle rectangle = new Rectangle();
		for (int x = 0; x < image.getWidth(null); x++) {
			for (int y = 0; y < image.getHeight(null); y++) {
				int rgb = ((BufferedImage) image).getRGB(x, y);
				Color c = new Color(rgb);
				if (c.getAlpha() > 10) {
					rectangle.setBounds(x, y, 1, 1);
					area.add(new Area(rectangle));
				}
			}
		}
		return area;
	}

	public static Area getArea(BufferedImage image, int maxTransparency) {
		Area area = new Area();
		Rectangle rectangle = new Rectangle();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int rgb = image.getRGB(x, y);
				rgb = rgb >>> 24;
				if (rgb >= maxTransparency) {
					rectangle.setBounds(x, y, 1, 1);
					area.add(new Area(rectangle));
				}
			}
		}
		return area;
	}

	public static Area getArea(Color color, int tolerance, BufferedImage image) {
		if (image == null)
			return null;
		Area area = new Area();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color pixel = new Color(image.getRGB(x, y));
				if (isIncluded(color, pixel, tolerance)) {
					Rectangle r = new Rectangle(x, y, 1, 1);
					area.add(new Area(r));
				}
			}
		}

		return area;
	}

	public static Area getAreaFastHack(BufferedImage image) {
		// Assumes Black as Shape Color
		if (image == null)
			return null;

		Area area = new Area();
		Rectangle r;
		int y1, y2;

		for (int x = 0; x < image.getWidth(); x++) {
			y1 = 99;
			y2 = -1;
			for (int y = 0; y < image.getHeight(); y++) {
				Color pixel = new Color(image.getRGB(x, y));
				// -16777216 entspricht RGB(0,0,0)
				if (pixel.getRGB() == -16777216) {
					if (y1 == 99) {
						y1 = y;
						y2 = y;
					}
					if (y > (y2 + 1)) {
						r = new Rectangle(x, y1, 1, y2 - y1);
						area.add(new Area(r));
						y1 = y;
						y2 = y;
					}
					y2 = y;
				}
			}
			if ((y2 - y1) >= 0) {
				r = new Rectangle(x, y1, 1, y2 - y1);
				area.add(new Area(r));
			}
		}

		return area;
	}

	public static boolean isIncluded(Color target, Color pixel, int tolerance) {
		int rT = target.getRed();
		int gT = target.getGreen();
		int bT = target.getBlue();
		int rP = pixel.getRed();
		int gP = pixel.getGreen();
		int bP = pixel.getBlue();
		return ((rP - tolerance <= rT) && (rT <= rP + tolerance) && (gP - tolerance <= gT) && (gT <= gP + tolerance));
	}
}
