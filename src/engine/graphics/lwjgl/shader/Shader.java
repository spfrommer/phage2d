package engine.graphics.lwjgl.shader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader {
	ShaderType m_type;
	int m_id;

	public Shader(ShaderType type) {
		setID(GL20.glCreateShader(type.getType()));
		setShaderType(type);
	}

	public Shader(Shader src) {
		setID(src.getID());
		setShaderType(src.getShaderType());
	}

	public Shader(String src, ShaderType type) {
		this(type);
		setSource(src);
	}

	public Shader(File src, ShaderType type) {
		this(type);
		setSource(src);
	}

	public Shader() {
	}

	public void setSource(File f) {
		try {
			setSource(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void setSource(InputStream in) {
		String source = readShaderSourceFile(in);
		setSource(source);
	}

	public void setSource(String src) {
		GL20.glShaderSource(getID(), src);
	}

	public String getSource() {
		return GL20.glGetShaderSource(getID(), GL20.GL_SHADER_SOURCE_LENGTH);
	}

	public void compile() throws ShaderCompileException {
		GL20.glCompileShader(getID());
		if (!isCompiled()) {
			throw new ShaderCompileException(getInfoLog());
		}
	}

	public boolean isCompiled() {
		return (GL20.glGetShaderi(getID(), GL20.GL_COMPILE_STATUS) == GL11.GL_TRUE);
	}

	public void delete() {
		GL20.glDeleteShader(getID());
	}

	public String getInfoLog() {
		return GL20.glGetShaderInfoLog(getID(), GL20.glGetShaderi(getID(), GL20.GL_INFO_LOG_LENGTH));
	}

	public int getID() {
		return m_id;
	}

	public ShaderType getShaderType() {
		return m_type;
	}

	public void setID(int id) {
		m_id = id;
	}

	public void setShaderType(ShaderType type) {
		m_type = type;
	}

	public static String readShaderSourceFile(InputStream in) {
		StringBuilder source = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				source.append(line).append('\n');
			}
			reader.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return source.toString();
	}

	public static class ShaderCompileException extends Exception {
		public ShaderCompileException(String r) {
			super(r);
		}
	}
}