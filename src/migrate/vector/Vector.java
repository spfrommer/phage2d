package migrate.vector;

import java.nio.FloatBuffer;

public abstract class Vector {
	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}	

	public abstract float lengthSquared();

	public abstract Vector negate();
	public abstract Vector normalise();
	public abstract Vector scale(float scale);

	public abstract void load(FloatBuffer buf);
	public abstract void store(FloatBuffer buf);
	
	public abstract Vector clone();
}