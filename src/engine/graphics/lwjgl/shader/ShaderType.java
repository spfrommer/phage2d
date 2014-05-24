package engine.graphics.lwjgl.shader;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

public enum ShaderType {
	FRAGMENT_SHADER(GL20.GL_FRAGMENT_SHADER),
		VERTEX_SHADER(GL20.GL_VERTEX_SHADER),
		GEOMETRY_SHADER(GL32.GL_GEOMETRY_SHADER),
		TESSELLATION_CONTROL_SHADER(GL40.GL_TESS_CONTROL_SHADER),
		TESSELLATION_EVALUATION_SHADER(GL40.GL_TESS_EVALUATION_SHADER);
	
	private int m_type;
	
	ShaderType(int type) {
		m_type = type;
	}
	
	public int getType() { return m_type; }
}