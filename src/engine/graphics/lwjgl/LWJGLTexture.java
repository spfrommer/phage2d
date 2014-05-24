package engine.graphics.lwjgl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;

public class LWJGLTexture {
	//Default is a white texture
	public static LWJGLTexture s_default;
	
	static {
		s_default = new LWJGLTexture();
		BufferedImage white  = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = (Graphics2D) white.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 1, 1);
		g2d.dispose();
		s_default.setImage(white);
		s_default.load();
	}
	
	private int m_id;
	private BufferedImage m_image;

	public LWJGLTexture(int id) {
		setID(id);
	}
	public LWJGLTexture() {
		setID(GL11.glGenTextures());
	}

	public int 	getID() {return m_id;}
	public void setID(int id) {m_id = id;}

	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getID());
	}
	public void unbind() {
		s_default.bind();
	}

	public void load() {
		LWJGLTextureLoader.loadTexture(getImage(), this);
	}

	public void delete() {
		GL11.glDeleteTextures(getID());
	}

	public BufferedImage getImage() { return m_image; }
	public void  setImage(BufferedImage image) { m_image = image; }

	public static LWJGLTexture s_createBlank(int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		LWJGLTexture t = new LWJGLTexture();
		t.setImage(image);
		t.load();
		return t;
	}
}