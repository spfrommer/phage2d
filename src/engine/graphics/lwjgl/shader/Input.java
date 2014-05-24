package engine.graphics.lwjgl.shader;

import engine.graphics.lwjgl.vector.Matrix4f;
import engine.graphics.lwjgl.vector.Vector2f;
import engine.graphics.lwjgl.vector.Vector3f;
import engine.graphics.lwjgl.vector.Vector4f;

public abstract class Input {
	public Input() {}
	public abstract void setValue(float val);
	public abstract void setValue(Vector2f val);
	public abstract void setValue(Vector3f val);
	public abstract void setValue(Vector4f val);
	public abstract void setValue(Matrix4f val);
}
