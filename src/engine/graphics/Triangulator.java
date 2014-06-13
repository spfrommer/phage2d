package engine.graphics;

import java.util.ArrayList;
import java.util.List;

import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.TriangulationPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;

import utils.shape.Triangle;
import engine.graphics.lwjgl.vector.Vector2f;

public class Triangulator {
	public static ArrayList<Triangle> s_triangulate(ArrayList<Vector2f> points) {
		ArrayList<PolygonPoint> polyPoints = new ArrayList<PolygonPoint>();
		for (int i = 0; i < points.size(); i++) {
			Vector2f vec = points.get(i);
			polyPoints.add(new PolygonPoint(vec.getX(), vec.getY()));
		}
		Polygon poly = new Polygon(polyPoints);
		Poly2Tri.triangulate(poly);
		List<DelaunayTriangle> tris = poly.getTriangles();
		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		for (DelaunayTriangle tri : tris) {
			TriangulationPoint[] pts = tri.points;
			Triangle t = new Triangle();
			t.set(0,  new Vector2f((float) pts[0].getX(), (float) pts[0].getY()));
			t.set(1,  new Vector2f((float) pts[1].getX(), (float) pts[1].getY()));
			t.set(2,  new Vector2f((float) pts[2].getX(), (float) pts[2].getY()));
			triangles.add(t);
		}
		return triangles;
	}
}
