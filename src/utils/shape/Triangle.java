package utils.shape;

import engine.graphics.lwjgl.vector.Vector2f;

public class Triangle {
	private Vector2f[] m_points = new Vector2f[3];
	
	public Triangle() {}
	public Triangle(Vector2f p1, Vector2f p2, Vector2f p3) {
		set(0, p1).set(1, p2).set(2, p3);
	}
	
	public Triangle set(int i, Vector2f point) {
		if (i > 2 || i < 0) throw new IllegalArgumentException("Out of bounds: " + i);
		m_points[i] = point;
		return this;
	}
	public Vector2f get(int i) { 
		if (i > 2 || i < 0) throw new IllegalArgumentException("Out of bounds: " + i);
		return m_points[i]; 
	}
}
