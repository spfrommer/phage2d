package utils;

import java.awt.geom.Point2D;

import engine.graphics.lwjgl.vector.Vector2f;

public class Point2f extends Point2D {
	private float m_x, m_y;
	public Point2f() { m_x = 0; m_y = 0; }
	public Point2f(float x, float y) {
		m_x = x;
		m_y = y;
	}
	public Point2f(Vector2f vec) { 
		m_x = vec.getX();
		m_y = vec.getY();
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
