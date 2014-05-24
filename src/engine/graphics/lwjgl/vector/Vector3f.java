package engine.graphics.lwjgl.vector;


import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Vector3f extends Vector {
	public float x, y, z;

	public Vector3f(float x, float y, float z) {
		setX(x);
		setY(y);
		setZ(z);
	}
	public Vector3f(Vector3f vector) {
		this(vector.getX(), vector.getY(), vector.getZ());
	}
	public Vector3f(FloatBuffer buf) {
		load(buf);
	}
	public Vector3f() {}
	public float getX() { return x; }
	public float getY() { return y; }
	public float getZ() { return z; }
	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = y; }
	public void setZ(float z) { this.z = z; }

	public void set(float x, float y) {
		setX(x);
		setY(y);
	}
	public void set(float x, float y, float z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	public float lengthSquared() {
		return x * x + y * y + z * z;
	}

	public Vector3f translate(float x, float y, float z) {
		set(getX() + x, getY() + y, getZ() + z);
		return this;
	}
	public Vector3f translate(Vector3f vector) {
		return translate(vector.x, vector.y, vector.z);
	}
	public Vector3f negate() {
		return negate(this);
	}
	public Vector3f negate(Vector3f dest) {
		if (dest == null) dest = new Vector3f();
		dest.x = -x;
		dest.y = -y;
		dest.z = -z;
		return dest;
	}
	public Vector3f normalise() {
		return normalise(this);
	}
	public Vector3f normalise(Vector3f dest) {
		if (dest == null) dest = new Vector3f();
		float length = length();
		dest.set(x/length, y/length, z/length);
		return dest;
	}
	public Vector3f scale(float scale) {
		x *= scale;
		y *= scale;
		z *= scale;
		return this;
	}
	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}
	public void load(FloatBuffer buf) {
		x = buf.get();
		y = buf.get();
		z = buf.get();
	}
	public void store(FloatBuffer buf) {
		buf.put(x);
		buf.put(y);
		buf.put(z);
	}
	public int getDataLength() { return 3; }

	

	public static Vector3f add(Vector3f left, Vector3f right, Vector3f dest) {
		if (dest == null) {
			dest = new Vector3f();
		}
		dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
		return dest;
	}
	//recursive add, add all the vectors in the array together into a big vector
	// that will be rewriten sometime
	public static Vector3f add(ArrayList<Vector3f> vectors) {
		Vector3f pre = new Vector3f(0, 0, 0);
		for(Vector3f vector : vectors) {
			add(pre, vector, pre);
		}
		return pre;
	}
	public static Vector3f sub(Vector3f left, Vector3f right, Vector3f dest) {
		if (dest == null) {
			dest = new Vector3f();
		}
		dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
		return dest;
	}
	public static Vector3f divide(Vector3f vector, float scalar, Vector3f dest) {
		if (dest == null) {
			dest = new Vector3f();
		}
		dest.x = vector.x/scalar;
		dest.y = vector.y/scalar;
		dest.z = vector.z/scalar;
		return dest;
	}
	public static float angle(Vector3f a, Vector3f b) {
		float dls = dot(a, b) / (a.length() * b.length());
		if (dls < -1f) dls = -1f;
		else if (dls > 1f) dls = 1f;
		return (float) Math.acos(dls);
	}
	public static float dot(Vector3f left, Vector3f right) {
		return left.getX() * right.getX() + left.getY() * right.getY() + left.getZ() * right.getZ();
	}
	public static Vector3f cross(Vector3f left, Vector3f right, Vector3f dest) {
		if (dest == null) dest = new Vector3f();
		
		dest.set(left.y * right.z - left.z * right.y,
				 right.x * left.z - right.z * left.x,
				 left.x * right.y - left.y * right.x);
		return dest;
	}

//for transforming vectors by Matrices

	public static Vector3f rotate(Vector3f vector, Matrix4f matrix) {
		float x[] = new float[3];
		float mat[][] = matrix.getFloats();
		for (int i = 0; i < 3; i++) {
			x[i] = 0;
			x[i] += mat[i][0] * vector.getX();
			x[i] += mat[i][1] * vector.getY();
			x[i] += mat[i][2] * vector.getZ();			
		}
		return new Vector3f(x[0], x[1], x[2]);
	}
	public static Vector3f translate(Vector3f vector, Matrix4f matrix) {
		float mat[][] = matrix.getFloats();
		Vector3f x = new Vector3f(vector.getX() + mat[0][3],
								  vector.getY() + mat[1][3],
								  vector.getZ() + mat[2][3]);
		return x;		
	}
	public static Vector3f transform(Vector3f vec, Matrix4f matrix) {
		Vector3f rotated = Vector3f.rotate(vec, matrix);
		Vector3f transformed = Vector3f.translate(rotated, matrix);
		return transformed;
	}


}