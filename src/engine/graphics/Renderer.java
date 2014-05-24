package engine.graphics;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.graphics.font.Font;
import engine.graphics.lwjgl.vector.Vector3f;


public interface Renderer {
	public void addPostProcessor(PostProcessor p);
	public void removePostProcessor(PostProcessor p);
	public ArrayList<PostProcessor> getPostProcessors();
	
	public void render(Renderable r);
	
	public void setColor(Color color);
	public Color getColor();
	
	public void setClip(Shape clip);
	public Shape getClip();
	
	public void fillRect(int x, int y, int width, int height);
	public void drawRect(int x, int y, int width, int height);
	
	public void drawLine(int x1, int y1, int x2, int y2);
	public void drawLineLoop(ArrayList<Point> points);
	
	public void fill(Shape shape);
	public void draw(Shape shape);
	
	public void drawPointLight(int x, int y, Color color, Vector3f attenuation);
	
	public void drawImage(BufferedImage img, int x, int y, int width, int height);
	public void drawImage(BufferedImage img, int x, int y);
	public void drawImage(BufferedImage img, AffineTransform trans);
	
	public void setFont(Font font);
	public Font getFont();
	
	/**
	 * Draws a single line of text
	 * Ignores new line characters
	 * xy coordinates are coordinates of the starting baseline
	 */
	public void drawString(String str, int x, int y);
	
	public void translate(int x, int y);
	public void rotate(float theta);
	public void rotate(float theta, int x, int y);
	public void scale(float sx, float sy);
	
	public void transform(AffineTransform trans);
	public void setTransform(AffineTransform trans);
	public AffineTransform getTransform();
	
	public void pushTransform();
	public void popTransform();
}
