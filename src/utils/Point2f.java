package utils;

import java.awt.geom.Point2D;

public class Point2f extends Point2D {
	private float m_x, m_y;
	public Point2f() { m_x = 0; m_y = 0; }
	public Point2f(float x, float y) {
		m_x = x;
		m_y = y;
	}
	@Override
	public double getX() {
		return m_x;
	}

	@Override
	public double getY() {
		return m_y;
	}
	@Override
	public String toString() {
		return "Point[" + getX() + ", " + getY() + "]";
	}
	@Override
	public void setLocation(double x, double y) {
		m_x = (float) x;
		m_y = (float) y;
	}
}
