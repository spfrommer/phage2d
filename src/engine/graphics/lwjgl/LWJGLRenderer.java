package engine.graphics.lwjgl;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import utils.Point2f;
import utils.shape.ShapeUtils;
import utils.shape.Triangle;
import engine.graphics.Color;
import engine.graphics.PostProcessor;
import engine.graphics.Renderable;
import engine.graphics.Renderer;
import engine.graphics.Triangulator;
import engine.graphics.font.Font;
import engine.graphics.font.Font.Glyph;
import engine.graphics.lwjgl.shader.FragmentShader;
import engine.graphics.lwjgl.shader.Program;
import engine.graphics.lwjgl.shader.Shader.ShaderCompileException;
import engine.graphics.lwjgl.shader.Uniform;
import engine.graphics.lwjgl.shader.VertexShader;
import engine.graphics.lwjgl.vector.Vector2f;
import engine.graphics.lwjgl.vector.Vector3f;

public class LWJGLRenderer implements Renderer {
	private static LWJGLRenderer s_instance;
	private static Program s_defaultProgram;
	private static int s_displayWidth;
	private static int s_displayHeight;

	private ArrayList<PostProcessor> m_postProcessors = new ArrayList<PostProcessor>();

	private ArrayList<LWJGLPointLight> m_pointLights = new ArrayList<LWJGLPointLight>();

	private HashMap<BufferedImage, LWJGLTexture> m_textures = new HashMap<BufferedImage, LWJGLTexture>();
	private Color m_color = new Color(1f, 1f, 1f, 1f);
	private Shape m_clip = null;
	private Font m_font;
	private Stack<Program> m_programStack = new Stack<Program>();
	private Program m_program;

	private LWJGLRenderer() {
	}

	@Override
	public void addPostProcessor(PostProcessor p) {
		m_postProcessors.add(p);
	}

	@Override
	public void removePostProcessor(PostProcessor p) {
		m_postProcessors.remove(p);
	}

	@Override
	public ArrayList<PostProcessor> getPostProcessors() {
		return m_postProcessors;
	}

	@Override
	public void render(Renderable r) {
		for (LWJGLPointLight light : m_pointLights) {
			light.apply();
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			r.render(this);
			light.clear();
		}
		s_defaultProgram.use();
	}

	public void pushProgram() {
		m_programStack.push(m_program);
	}

	public void popProgram() {
		Program p = m_programStack.pop();
		p.use();
	}

	public void setCurrentProgram(Program p) {
		m_program = p;
	}

	/*
	 * Color functions
	 */

	@Override
	public void setColor(Color color) {
		Uniform colorUni = m_program.getUniform("color");
		colorUni.setValue(color.toVector4f());

		m_color = color;
	}

	@Override
	public Color getColor() {
		return m_color;
	}
	public void clearClip() {
		GL11.glStencilMask(0xFF);
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
		m_clip = null;
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}
	public void occlude(Shape s) {
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		GL11.glColorMask(false, false, false, false);
		GL11.glDepthMask(false);
		GL11.glStencilMask(0xFF);
		GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xFF);
		GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);
		fill(s);
		// Back to normal rendering mode
		GL11.glColorMask(true, true, true, true);
		GL11.glDepthMask(true);
		GL11.glStencilMask(0x00);
		// draw where stencil's value is 0
		GL11.glStencilFunc(GL11.GL_EQUAL, 0, 0xFF);
	}
	/*
	 * Clip functions
	 */
	@Override
	public void setClip(Shape clip) {
		m_clip = clip;
		if (clip == null) {
			GL11.glDisable(GL11.GL_STENCIL_TEST);
			return;
		} else {
			GL11.glEnable(GL11.GL_STENCIL_TEST);
		}
		GL11.glColorMask(false, false, false, false);
		GL11.glDepthMask(false);
		GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xFF);
		GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);

		GL11.glStencilMask(0xFF);
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
		// Draw clip here
		fill(clip);

		// Back to normal rendering mode
		GL11.glColorMask(true, true, true, true);
		GL11.glDepthMask(true);
		GL11.glStencilMask(0x00);
		// draw where stencil's value is 0
		 GL11.glStencilFunc(GL11.GL_EQUAL, 0, 0xFF);
		 
		///* (nothing to draw) */
		// draw only where stencil's value is 1
		//GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
	}

	@Override
	public Shape getClip() {
		return m_clip;
	}

	/*
	 * Drawing functions
	 */

	@Override
	public void fillRect(float x, float y, float width, float height) {
		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x, y);

		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(x, y + height);

		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(x + width, y + height);

		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(x + width, y);
		GL11.glEnd();
	}

	@Override
	public void drawRect(float x, float y, float width, float height) {
		GL11.glBegin(GL11.GL_LINE_LOOP);

		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x, y);

		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(x, y + height);

		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(x + width, y + height);

		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(x + width, y);

		GL11.glEnd();
	}

	@Override
	public void drawLine(float x1, float y1, float x2, float y2) {
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x2, y2);
		GL11.glEnd();
	}
	@Override
	public void drawLines(ArrayList<Point2D> points) {
		GL11.glBegin(GL11.GL_LINES);
		for (int i = 0; i < points.size(); i++) {
			Point2D point = points.get(i);
			//If this is not the first, finish up the previous one
			if (i != 0 && i != (points.size() - 1)) GL11.glVertex2f((float) point.getX(), (float) point.getY());
			GL11.glVertex2f((float) point.getX(), (float) point.getY());
		}
		GL11.glEnd();
	}
	@Override
	public void drawLineLoop(ArrayList<Point2D> points) {
		GL11.glBegin(GL11.GL_LINE_LOOP);
		for (int i = 0; i < points.size(); i++) {
			Point2D point = points.get(i);
			GL11.glVertex2f((float) point.getX(), (float) point.getY());
		}
		GL11.glEnd();
	}

	@Override
	public void fill(Shape shape) {
		if (shape == null) return;
		ArrayList<Vector2f> points = ShapeUtils.getVectors(shape, true);
		ArrayList<Triangle> tris = Triangulator.s_triangulate(points);
		System.out.println("Filling: " + points.size());
		GL11.glBegin(GL11.GL_TRIANGLES);
		for (Triangle tri : tris) {
			GL11.glVertex2f(tri.get(0).getX(), tri.get(0).getY());
			GL11.glVertex2f(tri.get(1).getX(), tri.get(1).getY());
			GL11.glVertex2f(tri.get(2).getX(), tri.get(2).getY());
		}
		GL11.glEnd();
	}

	@Override
	public void draw(Shape shape) {
		PathIterator iterator = shape.getPathIterator(new AffineTransform());
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		float[] coords = new float[6];
		while (!iterator.isDone()) {
			iterator.currentSegment(coords);
			float x = (float) coords[0];
			float y = (float) coords[1];
			points.add(new Point2f(x, y));
			iterator.next();
		}
		points.remove(points.size() - 1);
		System.out.println("Drawing loop: " + points.size());
		drawLineLoop(points);
	}

	/*
	 * Light functions
	 */
	@Override
	public void drawPointLight(float x, float y, Color c, Vector3f attenuation) {
		Point2f transformed = new Point2f();
		getTransform().transform(new Point2f(x, y), transformed);
		Vector2f trans = new Vector2f((float) transformed.getX(), (float) transformed.getY());
		m_pointLights.add(new LWJGLPointLight(trans, c, attenuation));
	}

	/*
	 * Image drawing functions/helper functions
	 */

	public LWJGLTexture getTexture(BufferedImage image) {
		if (m_textures.containsKey(image))
			return m_textures.get(image);
		LWJGLTexture texture = new LWJGLTexture();
		texture.setImage(image);
		texture.load();
		m_textures.put(image, texture);
		return texture;
	}

	@Override
	public void drawImage(BufferedImage img, float x, float y, float width, float height) {
		LWJGLTexture texture = getTexture(img);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		texture.bind();
		fillRect(x, y, width, height);
		texture.unbind();
	}

	@Override
	public void drawImage(BufferedImage img, float x, float y) {
		drawImage(img, x, y, img.getWidth(), img.getHeight());
	}

	@Override
	public void drawImage(BufferedImage img, AffineTransform trans) {
		pushTransform();
		transform(trans);
		drawImage(img, 0, 0);
		popTransform();
	}

	/*
	 * String drawing functions
	 */

	@Override
	public void setFont(Font font) {
		m_font = font;
	}

	@Override
	public Font getFont() {
		return m_font;
	}

	@Override
	public void drawString(String str, float x, float y) {
		Font font = getFont();

		pushTransform();
		translate(x, y);

		char[] chars = str.toCharArray();
		for (char c : chars) {
			if (c == '\n' || c == '\t')
				continue;
			Glyph g = font.getGlyph(c);
			if (g != null) g.renderAndTranslate(this);
		}

		popTransform();
	}

	/*
	 * Transform functions
	 */

	@Override
	public void translate(float x, float y) {
		GL11.glTranslatef(x, y, 0);
	}

	@Override
	public void rotate(float theta) {
		// Rotate theta around the z axis
		GL11.glRotated(Math.toDegrees(theta), 0, 0, 1);
	}

	@Override
	public void rotate(float theta, float x, float y) {
		pushTransform();
		translate(x, y);
		rotate(theta);
		popTransform();
	}

	@Override
	public void scale(float sx, float sy) {
		GL11.glScaled(sx, sy, 0);
	}

	/*
	 * Functions for getting and setting affinetransforms as well as pop/pushing
	 */

	@Override
	public void transform(AffineTransform trans) {
		FloatBuffer buf = LWJGLUtils.toBuffer(LWJGLUtils.toMatrix(trans));
		GL11.glMultMatrix(buf);
	}

	public static float[][] getMatrix() {
		float mat[] = new float[16];
		ByteBuffer temp = ByteBuffer.allocateDirect(64);
		temp.order(ByteOrder.nativeOrder());
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, temp.asFloatBuffer());
		temp.asFloatBuffer().get(mat);
		float M[][] = new float[4][4];
		for (int i = 0; i < 16; i++) {
			// if (i % 4 == 0) {System.out.println("");}
			int n = i / 4;
			int m = i - 4 * n;
			M[m][n] = mat[i];
			// System.out.println("M[" + m + "," + n + "] = " + M[m][n]);
		}
		// System.out.println("");
		return M;
	}

	@Override
	public void setTransform(AffineTransform trans) {
		FloatBuffer buf = LWJGLUtils.toBuffer(LWJGLUtils.toMatrix(trans));
		GL11.glLoadMatrix(buf);
	}

	@Override
	public AffineTransform getTransform() {
		float[][] m = getMatrix();
		AffineTransform trans = new AffineTransform((double) m[0][0], (double) m[1][0], (double) m[0][1],
				(double) m[1][1], (double) m[0][3], (double) m[1][3]);
		return trans;
	}

	@Override
	public void pushTransform() {
		GL11.glPushMatrix();
	}

	@Override
	public void popTransform() {
		GL11.glPopMatrix();
	}

	public void update() {
		for (PostProcessor p : m_postProcessors) {
			GL11.glLoadIdentity();
			// TODO: Fix post processors
			p.run(this);
		}
		Display.update();
		// Clear the lights
		m_pointLights.clear();

		GL11.glLoadIdentity();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glLoadIdentity();
		doFrameSetup();
	}

	public static LWJGLRenderer instance() {
		if (s_instance == null) {
			s_instance = new LWJGLRenderer();
		}
		return s_instance;
	}

	// Runs all setup commands needed to render the next frame
	public static void doFrameSetup() {
		s_defaultProgram.use();
		LWJGLTexture.s_default.bind();
		// Reset the color uniform
		instance().setColor(instance().getColor());
	}

	/*
	 * Static opengl setup functions TODO: Move to display system
	 */

	public static void initDisplay(int width, int height) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		s_displayWidth = width;
		s_displayHeight = height;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		setupBlending();
		initLighting(s_displayWidth, s_displayHeight);
		// Initialize s_defaultProgram
		s_defaultProgram = new Program();

		try {
			FragmentShader fragment = new FragmentShader();
			fragment.setSource(new File(LWJGLRenderer.class.getResource("/shaders/default.frag").toURI()));
			VertexShader vertex = new VertexShader();
			vertex.setSource(new File(LWJGLRenderer.class.getResource("/shaders/default.vert").toURI()));
			fragment.compile();
			vertex.compile();

			s_defaultProgram.attachShader(fragment);
			s_defaultProgram.attachShader(vertex);
			s_defaultProgram.link();
			s_defaultProgram.validate();
		} catch (ShaderCompileException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		// Setup for the first frame
		doFrameSetup();
	}

	public static void setupBlending() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void initLighting(int width, int height) {
		LWJGLPointLight.init(width, height);
	}

	public static void initDisplayWithoutCanvas(int width, int height, boolean vsync) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setVSyncEnabled(vsync);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		initDisplay(width, height);
	}

	public static void destroyDisplay() {
		Display.destroy();
	}

}
