package engine.graphics.lwjgl.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import engine.graphics.lwjgl.LWJGLRenderer;

public class Program {
	int m_id;

	InputDictionary m_inputs = new InputDictionary();
	
	public Program() {
		setID(GL20.glCreateProgram());
	}
	public Program(int id) {
		setID(id);
	}
	public Program(Program program) {
		setID(program.getID());
	}

	public int getID() { return m_id; }
	public void setID(int id) {m_id = id;}

	public void attachShader(Shader shader) {
		GL20.glAttachShader(getID(), shader.getID());
	}
	public InputDictionary getInputs() {
		return m_inputs;
	}
	public Uniform getUniform(String location) {
		return new Uniform(this, location);
	}
	public Attribute getAttribute(String location) {
		return new Attribute(this, location);
	}
	public Uniform[] getUniforms() {
		int count = GL20.glGetProgrami(getID(), GL20.GL_ACTIVE_UNIFORMS);
		Uniform[] uniforms = new Uniform[count];
		for (int i = 0; i < count; i++) {
			int name_len = -1;
			String name = GL20.glGetActiveUniform(getID(), i, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH);
			uniforms[i] = getUniform(name);
		}
		return uniforms;
	}

	public void link() {
		GL20.glLinkProgram(getID());
	}

	public void validate() {
		GL20.glValidateProgram(getID());
	}
	public boolean isLinked() {
		return GL20.glGetProgrami(getID(), GL20.GL_LINK_STATUS) == GL11.GL_TRUE;
	}
	public boolean isValidated() {
		return GL20.glGetProgrami(getID(), GL20.GL_VALIDATE_STATUS) == GL11.GL_TRUE;
	}
	
	public void use() {
		GL20.glUseProgram(getID());
		LWJGLRenderer.getInstance().setCurrentProgram(this);
	}
	public void clear() {
		GL20.glUseProgram(0);
	}
	
	public String getInfoLog() {
		int length = GL20.glGetProgrami(getID(), GL20.GL_INFO_LOG_LENGTH);
		String log = GL20.glGetProgramInfoLog(getID(), length);
		return log;		
	}

	public void delete() {
		GL20.glDeleteProgram(getID());
	}
}