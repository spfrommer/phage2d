package utils.shape;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import utils.physics.Vector;

public class ShapeLoader {
	public static class NotAShapeException extends IOException {}

	public static Shape read(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String magic = reader.readLine();//Reads magic "shape"
		if (!magic.equals("shape")) {
			throw new NotAShapeException();
		}
		
		GeneralPath gp = new GeneralPath();
		
		ArrayList<Point2D> points = readVertices(reader);
		for (int i = 0; i < points.size(); i++) {
			Point2D point = points.get(i);
			if (i == 0) gp.moveTo(point.getX(), point.getY()); 
			else gp.lineTo(point.getX(), point.getY());
		}
		gp.closePath();
		
		return new Area(gp);
	}
	public static ArrayList<Point2D> readVertices(BufferedReader reader) throws IOException {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		String line;
		while((line = reader.readLine()) != null) {
			String[] halves = line.split(",");
			String half1 = halves[0];
			String half2 = halves[1];
			half1 = half1.trim();
			half2 = half2.trim();
			double x = Double.parseDouble(half1);
			double y = Double.parseDouble(half2);
			points.add(new Point((int) x, (int) y));
		}
		return points;
	}
	
	public static void main(String[] args) throws IOException {
		InputStream s = ShapeLoader.class.getResourceAsStream("test_shape.shape");
		read(s);
	}
}
