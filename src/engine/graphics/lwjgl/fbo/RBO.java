package engine.graphics.lwjgl.fbo;

import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

public class RBO {
	private int m_id = -1;
	
	public RBO() {
		setID(GL30.glGenRenderbuffers());
	}
	
	public void init(int width, int height) {
		bind();
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);
	}
	
	public void bind() {
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, getID());
	}
	public void unbind() {
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
	}
	
	public void setID(int id) { m_id = id; }
	public int getID() { return m_id; }
}
