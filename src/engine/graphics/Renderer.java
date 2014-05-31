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
	
	public void fillRect(float x, float y, float width, float height);
	public void drawRect(float x, float y, float width, float height);
	
	public void drawLine(float x1, float y1, float x2, float y2);
	public void drawLineLoop(ArrayList<Point> points);
	
	public void fill(Shape shape);
	public void draw(Shape shape);
	
	public void drawPointLight(float x, float y, Color color, Vector3f attenuation);
	
	public void drawImage(BufferedImage img, float x, float y, float width, float height);
	public void drawImage(BufferedImage img, float x, float y);
	public void drawImage(BufferedImage img, AffineTransform trans);
	
	public void setFont(Font font);
	public Font getFont();
	
	/**
	 * Draws a single line of text
	 * Ignores new line characters
	 * xy coordinates are coordinates of the starting baseline
	 */
	public void drawString(String str, float x, float y);
	
	public void translate(float x, float y);
	public void rotate(float theta);
	public void rotate(float theta, float x, float y);
	public void scale(float sx, float sy);
	
	public void transform(AffineTransform trans);
	public void setTransform(AffineTransform trans);
	public AffineTransform getTransform();
	
	public void pushTransform();
	public void popTransform();
}
