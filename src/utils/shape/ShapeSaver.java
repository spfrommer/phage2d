package utils.shape;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class ShapeSaver {
	public static void write(Shape s, OutputStream out) throws IOException {
		PrintWriter writer = new PrintWriter(out);
		writer.println("shape");
		ArrayList<Point2D> points = ShapeUtils.getPoints(s, true);
		for (Point2D point : points) {
			writer.println(point.getX() + ", " + point.getY());
		}
		writer.flush();
	}

	public static void main(String[] args) throws IOException {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point(0, 0));
		points.add(new Point(1, 0));
		points.add(new Point(0, 1));
		Shape s = ShapeUtils.createShape(points);

		File file = null;
		try {
			file = new File(ShapeSaver.class.getResource("/").toURI());
			file = new File(file.getParent(), "src/shape/test_shape.shape");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		FileOutputStream output = new FileOutputStream(file);
		System.out.println("Writing to file: " + file.getAbsolutePath());
		write(s, output);
	}
}
