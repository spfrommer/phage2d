package utils.shape;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import utils.physics.Vector;

public class ShapeUtils {
	private ShapeUtils() {
	}

	/**
	 * Creates a shape from an arraylist of points
	 * 
	 * @param points
	 * @return
	 */
	public static Shape createShape(ArrayList<Point2D> points) {
		GeneralPath gp = new GeneralPath();
		for (int i = 0; i < points.size(); i++) {
			Point2D point = points.get(i);
			if (i == 0)
				gp.moveTo(point.getX(), point.getY());
			else
				gp.lineTo(point.getX(), point.getY());
		}
		gp.closePath();

		return gp;
	}

	/**
	 * Gets an arraylist of points from a shape
	 * 
	 * @param s
	 * @return
	 */
	public static ArrayList<Point2D> getPoints(Shape s) {
		ArrayList<Point2D> points = getPointLoop(s);

		// Remove closing segment
		if (points.size() != 0)
			points.remove(points.size() - 1);
		return points;
	}

	/**
	 * Gets a point loop including the first point
	 * 
	 * @param s
	 * @return
	 */
	public static ArrayList<Point2D> getPointLoop(Shape s) {
		ArrayList<Point2D> points = new ArrayList<Point2D>();

		ShapeIterator iterator = new ShapeIterator(s, null);
		while (iterator.hasNext()) {
			Point2D point = iterator.next();
			points.add(point);
		}
		return points;
	}

	/**
	 * Tests if two shapes intersect
	 * 
	 * @param shape1
	 * @param shape2
	 * @return
	 */
	public static boolean testIntersection(Shape shape1, Shape shape2) {
		Area areaA = new Area(shape1);
		areaA.intersect(new Area(shape2));
		return !areaA.isEmpty();
	}

	/**
	 * Projects the maximum Vector that entails the Shape within 1 pixel
	 * accuracy
	 * 
	 * @param shape
	 * @param startingPoint
	 * @param projection
	 * @return
	 */
	public static Vector project(Shape shape, Vector startingPoint, Vector direction) {
		Vector projectionIncrement = direction.scalarMultiply(1 / direction.length());
		Vector totalProjection = new Vector(startingPoint);
		totalProjection = totalProjection.add(projectionIncrement);
		while (shape.contains(totalProjection.getX(), totalProjection.getY())) {
			totalProjection = totalProjection.add(projectionIncrement);
		}
		totalProjection = totalProjection.subtract(projectionIncrement);

		return (totalProjection.subtract(startingPoint));
	}
}
