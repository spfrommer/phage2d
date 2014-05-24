package engine.graphics.lwjgl.fbo;

import org.lwjgl.opengl.GL30;

public enum FBOAttachment {
	COLOR0(GL30.GL_COLOR_ATTACHMENT0),
	COLOR1(GL30.GL_COLOR_ATTACHMENT1),
	COLOR2(GL30.GL_COLOR_ATTACHMENT2),
	DEPTH(GL30.GL_DEPTH_ATTACHMENT),
	STENCIL(GL30.GL_STENCIL_ATTACHMENT);
	
	private int m_type;
	
	FBOAttachment(int type) {
		m_type = type;
	}
	
	public int getAttachmentType() { return m_type; }
}
