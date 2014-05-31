package utils;

import java.awt.geom.Point2D;

public class Point2d extends Point2D {
	private double m_x, m_y;
	public Point2d(double x, double y) {
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
	public void setLocation(double x, double y) {
		m_x = x;
		m_y = y;
	}
}