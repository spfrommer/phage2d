package migrate.vector;

import java.nio.FloatBuffer;

public class Vector4f extends Vector {
	public float x, y, z, w;

	public Vector4f() {
		super();
	}
	public Vector4f(Vector4f src) {
		super();
		set(src);
	}
	public Vector4f(Vector3f vec, float w) {
		set(vec.getX(), vec.getY(), vec.getZ(), w);
	}
	public Vector4f(float x, float y, float z, float w) {
		super();
		set(x, y, z, w);
	}
	public Vector4f(float xyz) {
		set(xyz, xyz, xyz, 1.0f);
	}
	
	public float getX() {return x;}
	public float getY() {return y;}
	public float getZ() {return z;}
	public float getW() {return w;}
	public void  setX(float x) {this.x = x;}
	public void  setY(float y) {this.y = y;}
	public void  setZ(float z) {this.z = z;}
	public void  setW(float w) {this.w = w;}

	public Vector4f set(Vector4f src) {
		setX(src.getX());
		setY(src.getY());
		setZ(src.getZ());
		setW(src.getW());
		return this;
	}
	public Vector4f set(float x, float y) {
		setX(x);
		setY(y);
		return this;
	}
	public Vector4f set(float x, float y, float z) {
		setX(x);
		setY(y);
		setZ(z);
		return this;
	}
	public Vector4f set(float x, float y, float z, float w) {
		setX(x);
		setY(y);
		setZ(z);
		setW(w);
		return this;
	}


	public float lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}
	public Vector4f negate() {
		return negate(this);
	}
	public Vector4f negate(Vector4f dest) {
		return negate(this, dest);
	}
	public Vector4f normalise() {
		return normalise(this);
	}
	public Vector4f normalise(Vector4f dest) {
		return normalise(this, dest);
	}

	public Vector4f add(Vector4f right) {
		return add(right, this);
	}
	public Vector4f add(Vector4f right, Vector4f dest) {
		return add(this, right, dest);
	}
	public Vector4f sub(Vector4f right) {
		return sub(right, this);
	}

	public Vector4f sub(Vector4f right, Vector4f dest) {
		return sub(this, right, dest);
	}
	public Vector4f translate(float x, float y, float z, float w) {
		return add(new Vector4f(x, y, z, w));
	}
	public Vector4f translate(Vector4f trans) {
		return add(trans);
	}
	public Vector4f scale(float scale) {
		return scale(scale, this);
	}
	public Vector4f scale(float scale, Vector4f dest) {
		return scale(this, scale, dest);
	}
	public float    dot(Vector4f right) {
		return dot(this, right);
	}
	public float    angle(Vector4f b) {
		return angle(this, b);
	}

	public void store(FloatBuffer buf) {
		buf.put(x).put(y).put(z).put(w);
	}
	public void load(FloatBuffer buf) {
		x = buf.get();
		y = buf.get();
		z = buf.get();
		w = buf.get();
	}

	public String toString() {
		return "[" + x + ", " + y + ", " + z + ", " + w + "]";
	}
	
	public Vector4f clone() {
		return new Vector4f(this);
	}

	public static Vector4f normalise(Vector4f src, Vector4f dest) {
		if (dest == null) dest = new Vector4f();
		float length = src.length();
		dest.set(src.getX()/length, src.getY()/length, 
				 src.getZ()/length, src.getW()/length);
		return dest;
	}
	public static Vector4f negate(Vector4f src, Vector4f dest) {
		if (dest == null) dest = new Vector4f();
		dest.setX(-src.getX());
		dest.setY(-src.getY());
		dest.setZ(-src.getZ());
		dest.setW(-src.getW());
		return dest;
	}
	
	public static Vector4f add(Vector4f left, Vector4f right, Vector4f dest) {
		if (dest == null) dest = new Vector4f();
		dest.set(left.getX() + right.getX(), left.getY() + right.getY(),
				 left.getZ() + right.getZ(), left.getW() + right.getW());
		return dest;
	}
	public static Vector4f sub(Vector4f left, Vector4f right, Vector4f dest) {
		if (dest == null) dest = new Vector4f();
		dest.set(left.getX() - right.getX(), left.getY() - right.getY(),
				 left.getZ() - right.getZ(), left.getW() - right.getW());
		return dest;
	}
	public static Vector4f scale(Vector4f src, float scale, Vector4f dest) {
		if (dest == null) dest = new Vector4f();
		dest.set(src.getX() * scale, src.getY() * scale, 
				 src.getZ() * scale, src.getW() * scale);
		return dest;
	}
	public static Vector4f power(Vector4f vec, float power, Vector4f dest) {
		if (dest == null) dest = new Vector4f();
		dest.set((float) (Math.pow(vec.getX(), power)), 
				 (float) (Math.pow(vec.getY(), power)),
				 (float) (Math.pow(vec.getZ(), power)));
		return dest;
	}
	public static float dot(Vector4f left, Vector4f right) {
		return left.getX() * right.getX() + left.getY() * right.getY() +
			left.getZ() * right.getZ() + left.getW() + right.getW();
	}
	public static float angle(Vector4f a, Vector4f b) {
		float dls = dot(a, b) / (a.length() * b.length());
		if (dls < -1f) {
			dls = -1f;
		} else if (dls > 1.0f) {
			dls = 1.0f;
		}
		return (float) Math.acos(dls);
	}
}