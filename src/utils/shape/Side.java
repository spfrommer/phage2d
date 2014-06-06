package utils.shape;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import utils.Point2f;
import engine.graphics.lwjgl.vector.Vector2f;

public class Side {
	private Vector2f m_start;
	private Vector2f m_end;
	//If flase, ccw, if true cw
	private boolean m_cwWinding = true;
	
	public Side(Vector2f start, Vector2f end) {
		m_start = start;
		m_end = end;
	}
	public Side(Vector2f start, Vector2f end, boolean cw) {
		m_start = start;
		m_end = end;
		m_cwWinding = cw;
	}
	
	public void setWinding(boolean cw) {
		m_cwWinding = cw;
	}
	public Vector2f getStart() { return m_start; }
	public Vector2f getEnd() { return m_end; }
	/**
	 * Gets whether the winding is cw or ccw
	 * @return cw if true, ccw if false
	 */
	public boolean getWinding() { return m_cwWinding; }
	
	public Vector2f getDiff() {
		return Vector2f.sub(m_end, m_start, null);
	}
	public Vector2f getMidPoint() {
		return Vector2f.add(m_end, m_start, null).scale(0.5f);
	}
	public Vector2f getPerpendicular() {
		return getPerpendicular(m_cwWinding);
	}
	public Vector2f getPerpendicular(boolean clockwise) {
		return getDiff().perpendicular(clockwise);
	}
	public String toString() {
		return "Side" + getStart() + " to " + getEnd();
	}
	public static ArrayList<Vector2f> getPoints(ArrayList<Side> sides) {
		HashMap<Vector2f, Side> startPoints = new HashMap<Vector2f, Side>();
		HashMap<Vector2f, Side> endPoints = new HashMap<Vector2f, Side>();
		for (Side s : sides) {
			startPoints.put(s.getStart(), s);
			endPoints.put(s.getEnd(), s);
		}
		//Now find the very first side by going backwards
		Side first = sides.get(0);
		while (endPoints.containsKey(first.getStart())) {
			first = endPoints.get(first.getStart());
			
		}
		ArrayList<Vector2f> points = new ArrayList<Vector2f>();
		Side current = first;
		while (current != null) {
			points.add(current.getStart());
			//Find the next side
			if (!startPoints.containsKey(current.getEnd())) {
				points.add(current.getEnd());
			} else if (points.contains(current.getEnd())) break;
			current = startPoints.get(current.getEnd());
		}
		
		return points;
	}
}
