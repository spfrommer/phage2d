package utils.shape;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import utils.Point2f;
import utils.physics.Vector;
import engine.graphics.lwjgl.vector.Vector2f;

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
	public static ArrayList<Point2D> getPoints(Shape s, boolean clockwise) {
		ArrayList<Point2D> points = getPointLoop(s);

		// Remove closing segment
		if (points.size() != 0) points.remove(points.size() - 1);
		if (!clockwise) Collections.reverse(points);
		return points;
	}
	/**
	 * Gets an arraylist of vecs from a shape
	 * 
	 * @param s
	 * @return
	 */
	public static ArrayList<Vector2f> getVectors(Shape shape, boolean clockwise) {
		PathIterator iterator = shape.getPathIterator(new AffineTransform());
		ArrayList<Vector2f> points = new ArrayList<Vector2f>();
		float[] coords = new float[6];
		while (!iterator.isDone()) {
			iterator.currentSegment(coords);
			float x = (float) coords[0];
			float y = (float) coords[1];
			points.add(new Vector2f(x, y));
			iterator.next();
		}

		// Remove closing segment
		if (points.size() != 0) points.remove(points.size() - 1);
		if (!clockwise) Collections.reverse(points);
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
	public static ArrayList<Side> getSides(Shape s, boolean clockwise) {
		ArrayList<Side> sides = new ArrayList<Side>();
		ArrayList<Point2D> points = getPoints(s, clockwise);
		Point2D previous = null;
		for (Point2D point : points) {
			if (previous != null) sides.add(new Side(new Vector2f((float) previous.getX(), (float) previous.getY()), 
														new Vector2f((float) point.getX(), (float) point.getY()), clockwise));
			previous = point;
		}
		return sides;
	}
	/**
	 * Sides must be of a shape
	 * This function only adds the first point of each side
	 * @param sides
	 * @return
	 */
	public static ArrayList<Point2D> getPoints(ArrayList<Side> sides) {
		HashMap<Vector2f, Side> startPoints = new HashMap<Vector2f, Side>();
		HashMap<Vector2f, Side> endPoints = new HashMap<Vector2f, Side>();
		for (Side s : sides) {
			startPoints.put(s.getStart(), s);
			endPoints.put(s.getEnd(), s);
		}
		//Now find the very first side by going backwards
		Side first = sides.get(0);
		while (endPoints.containsKey(first.getStart())) first = endPoints.get(first.getStart());
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		Side current = first;
		while (current != null) {
			points.add(new Point2f(current.getStart().getX(), current.getStart().getY()));
			//Find the next side
			if (!startPoints.containsKey(current.getEnd())) {
				points.add(new Point2f(current.getEnd().getX(), current.getEnd().getY()));
			}
			current = startPoints.get(current.getEnd());
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
	 * Removes all sides faces away or to the position
	 * @param sides
	 * @param sLoc
	 * @param pos
	 * @param front
	 * @return
	 */
	public static ArrayList<Side> cullSides(ArrayList<Side> sides, Vector2f sOff, Vector2f pos, boolean front) {
		ArrayList<Side> passed = new ArrayList<Side>();
		for (Side side : sides) {
			boolean facingTowards = facing(side, sOff, pos);
			if (front && !facingTowards || !front && facingTowards) passed.add(side); 
		}
		return passed;
	}
	public static boolean facing(Side side, Vector2f sPos, Vector2f pos) {
		Vector2f perpendicular = side.getPerpendicular();

		Vector2f start = Vector2f.add(side.getStart(), sPos, null);
		Vector2f end = Vector2f.add(side.getEnd(), sPos, null);
		//Overwrite into start and world, now we have the start/end to pos vectors...
		Vector2f.sub(pos, start, start);
		Vector2f.sub(pos, end, end);
		//Now dot with perpendicular to check if facing away or to
		boolean facingTowards = Vector2f.dot(start, perpendicular) >= 0 || Vector2f.dot(end, perpendicular) >= 0;
		return facingTowards;
	}
	public static ArrayList<Point2D> createShadowArea(Shape s, Vector2f loc, Vector2f lightPos, float infinity) {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		
		ArrayList<Side> sides = getSides(s, false);
		//System.out.println("Got Sides");
		ArrayList<Side> back = cullSides(sides, loc, lightPos, true);
		//System.out.println("Culled sides");
		ArrayList<Vector2f> backPoints = Side.getPoints(back);
		System.out.println("Points: " + backPoints.size() + " Sides: " + back.size());
		//Add the backPoints to the point list
		for (Vector2f vec : backPoints) {
			points.add(new Point2f(vec.getX(), vec.getY()));
		}
		//Now reverse the list of points and add them extruded to infinity
		Collections.reverse(backPoints);
		for (Vector2f vec : backPoints) {
			points.add(infinityProject(vec, lightPos, infinity));
		}
		return points;
	}
	public static Point2D infinityProject(Vector2f point, Vector2f start, float infinity) {
		Vector2f pointToStart = Vector2f.sub(point, start, null);
		pointToStart.scale(infinity);
		return new Point2f(pointToStart.getX(), pointToStart.getY());
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
