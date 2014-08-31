package migrate.vector;

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
	public abstract Matrix load(float[][] m);
	public abstract Matrix load(FloatBuffer buf);
	public abstract Matrix store(FloatBuffer buf);
	public abstract Matrix loadTranspose(FloatBuffer buf);
	public abstract Matrix storeTranspose(FloatBuffer buf);

	public abstract float[][] toFloats();
	
	public abstract Matrix clone();
}