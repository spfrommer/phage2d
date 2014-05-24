package utils.physics;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import org.dyn4j.geometry.Vector2;

import utils.AngleUtils;

public class Vector {
	protected double m_x;
	protected double m_y;

	public Vector(double x, double y) {
		m_x = x;
		m_y = y;
	}

	public Vector(Vector v) {
		m_x = v.getX();
		m_y = v.getY();
	}

	public Vector(Vector2 v) {
		m_x = v.x;
		m_y = v.y;
	}

	public Vector(Point2D p) {
		m_x = p.getX();
		m_y = p.getY();
	}

	public Vector2 toPhysicsVector() {
		return (new Vector2(m_x, m_y));
	}

	// Takes angle in degrees
	public static Vector normalVector(double angle) {
		return new Vector(Math.sin(Math.toRadians(angle)), Math.cos(Math.toRadians(angle)));
	}

	// takes angle in degrees between -180 to +180
	public static Vector dyn4jNormalVector(double angle) {
		return normalVector(-angle);
	}

	public Vector add(Vector v) {
		return (new Vector(m_x + v.getX(), m_y + v.getY()));
	}

	public Vector subtract(Vector v) {
		return (new Vector(m_x - v.getX(), m_y - v.getY()));
	}

	public Vector scalarMultiply(double d) {
		return (new Vector(m_x * d, m_y * d));
	}

	// Takes angle in degrees
	public Vector rotate(double angle) {
		double[] pt = { m_x, m_y };
		AffineTransform.getRotateInstance(Math.toRadians(-angle), 0, 0).transform(pt, 0, pt, 0, 1);
		return (new Vector(pt[0], pt[1]));

	}

	public Vector transform(AffineTransform at) {
		Point2D.Double coords = new Point2D.Double(m_x, m_y);
		Point2D.Double dest = new Point2D.Double();
		at.transform(coords, dest);

		return (new Vector(dest));
	}

	public Vector inverseTransform(AffineTransform at) throws NoninvertibleTransformException {
		Point2D.Double coords = new Point2D.Double(m_x, m_y);
		Point2D.Double dest = new Point2D.Double();
		at.inverseTransform(coords, dest);

		return (new Vector(dest));
	}

	// Returns angle in degrees
	public double absAngleTo(Vector other) {
		return AngleUtils.absAngle(Math.toDegrees(Math.atan2(m_y, m_x) - Math.atan2(other.getY(), other.getX())));
	}

	// Returns angle in degrees
	public double angleTo(Vector other) {
		return Math.toDegrees(Math.atan2(m_y, m_x) - Math.atan2(other.getY(), other.getX()));
	}

	// Returns angle in degrees
	public double angle() {
		return (new Vector(0, 1)).absAngleTo(this);
	}

	public double getX() {
		return m_x;
	}

	public double getY() {
		return m_y;
	}

	public double length() {
		return Math.sqrt(m_x * m_x + m_y * m_y);
	}

	public double quickLength() {
		return m_x * m_x + m_y * m_y;
	}

	public Point toPoint() {
		return new Point((int) m_x, (int) m_y);
	}

	@Override
	public String toString() {
		return ("[" + m_x + ", " + m_y + "]");
	}

	private final int DOT_WIDTH = 10;

	public Shape getPaintable() {
		double width = DOT_WIDTH;
		Ellipse2D.Double toPaint = new Ellipse2D.Double((int) (m_x - width / 2), (int) (m_y - width / 2), (int) width,
				(int) width);
		return toPaint;
	}

	@Override
	public Vector clone() {
		return new Vector(m_x, m_y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(m_x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(m_y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (!this.getClass().isInstance(obj))
			return false;
		Vector other = (Vector) obj;
		if (Double.doubleToLongBits(m_x) != Double.doubleToLongBits(other.m_x))
			return false;
		if (Double.doubleToLongBits(m_y) != Double.doubleToLongBits(other.m_y))
			return false;
		return true;
	}
}
