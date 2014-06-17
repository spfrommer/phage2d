package test;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import utils.image.ImageUtils;
import utils.shape.ShapeUtils;
import engine.graphics.Color;
import engine.graphics.Renderable;
import engine.graphics.Renderer;
import engine.graphics.lwjgl.LWJGLDisplay;
import engine.graphics.lwjgl.LWJGLKeyboard;
import engine.graphics.lwjgl.vector.Vector2f;
import engine.graphics.lwjgl.vector.Vector3f;
import engine.inputs.keyboard.Keyboard;

public class ShadowTest implements Renderable {
	private static BufferedImage TANK_BODY = ImageUtils.readImage("/images/chassis1.png");
	public Vector2f m_lightPos = new Vector2f(100f, 100f);
	public void run() {
		LWJGLDisplay display = new LWJGLDisplay(1024, 1024);
		LWJGLKeyboard keyboard = LWJGLKeyboard.instance();
		display.init();
		while(!display.destroyRequested()) {
			renderAll(display.getRenderer());
			updateLightPos(keyboard);
			display.update(60);
		}
	}
	public void renderAll(Renderer r) {
		r.drawPointLight(m_lightPos.getX(), m_lightPos.getY(), new Color(1, 1, 1, 1), new Vector3f(0.5f, 0.0005f, 0.0000001f));
		r.render(this);
		r.setColor(Color.BLUE);
		r.fillRect(m_lightPos.getX() - 25, m_lightPos.getY() - 25, 50, 50);
		r.setColor(Color.WHITE);
	}
	public void updateLightPos(Keyboard k) {
		if (k.isKeyPressed('w')) {
			m_lightPos.setY(m_lightPos.getY() - 10);
		} else if (k.isKeyPressed('d')) {
			m_lightPos.setX(m_lightPos.getX() + 10);
		} else if (k.isKeyPressed('a')) {
			m_lightPos.setX(m_lightPos.getX() - 10);
		} else if (k.isKeyPressed('s')) {
			m_lightPos.setY(m_lightPos.getY() + 10);
		}
	}
	@Override
	public void render(Renderer r) {
		r.setColor(Color.WHITE);
		r.clearClip();
		//r.clearClip();
		Rectangle rect = new Rectangle(200, 200, 100, 100);
		System.out.println(ShapeUtils.getVectors(rect, true));
		ArrayList<Point2D> shape = ShapeUtils.createShadowArea(rect, m_lightPos, 500);
		Shape s  = ShapeUtils.createShape(shape);
		r.occlude(s);
		r.fill(rect);
		//r.draw(s);
		//r.fill(s);

		/*for (Side s : sides) {
			r.drawLine(s.getStart().getX(), s.getStart().getY(), s.getEnd().getX(), s.getEnd().getY());
			Vector2f midpt = s.getMidPoint();
			Vector2f perp = s.getPerpendicular();
			r.drawLine(midpt.getX(), midpt.getY(), midpt.getX() + perp.getX(), midpt.getY() + perp.getY());
		}*/
		r.drawImage(TANK_BODY, 0, 0, 1024, 1024);
		
	}
	
	public static void main(String[] args) {
		new ShadowTest().run();
	}
}
