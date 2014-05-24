package engine.graphics.lwjgl.shader;

import engine.graphics.lwjgl.vector.Matrix4f;
import engine.graphics.lwjgl.vector.Vector2f;
import engine.graphics.lwjgl.vector.Vector3f;
import engine.graphics.lwjgl.vector.Vector4f;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class Uniform extends Input {
	private int m_id;

	public Uniform(Program program, String loc) {
		setID(GL20.glGetUniformLocation(program.getID(), loc));
		if (getID() == -1) throw new IllegalArgumentException("Uniform name not found: " + loc);
	}
	public Uniform(Uniform uniform) {
		setID(uniform.getID());
	}
	public Uniform() {}

	public int getID() {return m_id;}
	public void setID(int id) {m_id = id;}

	public void setValue(float val) {
		GL20.glUniform1f(getID(), val);
	}
	public void setValue(Vector2f vec) {
		GL20.glUniform2f(getID(), vec.getX(), vec.getY());
	}
	public void setValue(Vector3f vec) {
		GL20.glUniform3f(getID(), vec.getX(), vec.getY(), vec.getZ());
	}
	public void setValue(Vector4f vec) {
		GL20.glUniform4f(getID(), vec.getX(), vec.getY(), vec.getZ(), vec.getW());
	}
	public void setValue(Matrix4f mat) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		mat.store(buffer);
		buffer.flip();
		GL20.glUniformMatrix4(getID(), false, buffer);
	}
}