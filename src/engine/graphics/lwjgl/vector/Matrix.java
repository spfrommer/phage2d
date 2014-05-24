package engine.graphics.lwjgl.vector;

import java.nio.FloatBuffer;

public abstract class Matrix {
	public Matrix() {
		super();
	}
	
	public abstract float determinant();

	public abstract Matrix invert();
	public abstract Matrix negate();
	public abstract Matrix transpose();

	public abstract Matrix setIdentity();
	public abstract Matrix setZero();
	public abstract Matrix setFloats(float[][] m);
	public abstract float[][] getFloats();
	public abstract void load(FloatBuffer buf);
	public abstract void store(FloatBuffer buf);
	public abstract Matrix loadTranspose(FloatBuffer buf);
	public abstract Matrix storeTranspose(FloatBuffer buf);
}