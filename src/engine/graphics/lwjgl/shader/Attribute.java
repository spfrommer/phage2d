package engine.graphics.lwjgl.shader;


import engine.graphics.lwjgl.vector.Matrix4f;
import engine.graphics.lwjgl.vector.Vector2f;
import engine.graphics.lwjgl.vector.Vector3f;
import engine.graphics.lwjgl.vector.Vector4f;

import org.lwjgl.opengl.GL20;

public class Attribute extends Input {
	private int m_id;

	public Attribute(Program program, String loc) {
		setID(GL20.glGetAttribLocation(program.getID(), loc));
	}
	public Attribute(Attribute attribute) {
		setID(attribute.getID());
	}
	public Attribute() {}

	public int getID() {return m_id;}
	public void setID(int id) {m_id = id;}
	public int getIndex() { return m_id; }//index and id are the same, I hope

	public void setValue(float val) {
		GL20.glVertexAttrib1f(getID(), val);
	}
	public void setValue(Vector2f vec) {
		GL20.glVertexAttrib2f(getID(), vec.getX(), vec.getY());
	}
	public void setValue(Vector3f vec) {
		GL20.glVertexAttrib3f(getID(), vec.getX(), vec.getY(), vec.getZ());
	}
	public void setValue(Vector4f vec) {
		GL20.glVertexAttrib4f(getID(), vec.getX(), vec.getY(), vec.getZ(), vec.getW());
	}
	public void setValue(Matrix4f vec) {
		
	}
}