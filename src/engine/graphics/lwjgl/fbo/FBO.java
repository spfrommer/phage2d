package engine.graphics.lwjgl.fbo;

import engine.graphics.lwjgl.LWJGLTexture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;

public class FBO {
	private int m_id;
	
	public FBO() {
		setID(GL30.glGenFramebuffers());
	}
	
	public void bind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, getID());
	}
	public void unbind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	public void attachTexture(LWJGLTexture tex, FBOAttachment attachment) {
		bind();
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, 
				attachment.getAttachmentType(), 
				GL11.GL_TEXTURE_2D, tex.getID(), 0);
	}
	public void attachRBO(RBO rbo, FBOAttachment attachment) {
		bind();
		rbo.bind();
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, rbo.getID());
	}
	
	public void checkComplete() {
		int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
		switch (status) {
			case GL30.GL_FRAMEBUFFER_COMPLETE:
				break;
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
				throw new RuntimeException( "FrameBuffer: " + getID()
						+ ", has an incomplete attachment" );
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
				throw new RuntimeException( "FrameBuffer: " + getID()
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT exception" );
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
				throw new RuntimeException( "FrameBuffer: " + getID()
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER exception" );
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
				throw new RuntimeException( "FrameBuffer: " + getID()
						+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER exception" );
			default:
				throw new RuntimeException( "Unexpected reply for framebuffer: " + getID());
		}
	}
	
	public void setID(int id) { m_id = id; }
	public int getID() { return m_id; }
}
