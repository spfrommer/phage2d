package utils.shape;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import utils.Point2f;

public class ShapeIterator implements Iterator<Point2D>, Iterable<Point2D> {
	Shape m_shape = null;
	PathIterator m_iterator = null;

	Point m_previous = null;
	int m_count = 0;

	public ShapeIterator(Shape s, AffineTransform trans) {
		m_shape = s;
		m_iterator = s.getPathIterator(trans);
	}

	@Override
	public boolean hasNext() {
		return !m_iterator.isDone();// If we are done, it will return false,
									// otherwise true
	}

	@Override
	public Point2D next() {
		if (hasNext()) {
			double[] point = new double[2];
			int type = m_iterator.currentSegment(point);
			m_count++;
			m_iterator.next();
			return new Point2f((int) point[0], (int) point[1]);
		} else {
			return null;
		}
	}

	@Override
	public void remove() {
		throw new AssertionError();
	}

	@Override
	public Iterator<Point2D> iterator() {
		return this;
	}

	public static void main(String[] args) {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2f(0, 0));
		points.add(new Point2f(1, 0));
		points.add(new Point2f(0, 1));
		Shape s = ShapeUtils.createShape(points);
		ShapeIterator iterator = new ShapeIterator(s, null);
		for (Point2D point : iterator) {
			System.out.println(point.getX() + ", " + point.getY());
		}
	}
}