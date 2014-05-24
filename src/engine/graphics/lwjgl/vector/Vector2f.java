package engine.graphics.lwjgl.vector;

import java.nio.FloatBuffer;

public class Vector2f extends Vector {
	public float x, y;

	public Vector2f() {}
	public Vector2f(float x, float y) {
		set(x, y);
	}
	public Vector2f(Vector2f src) {
		set(src);
	}

	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = y; }

	public float getX() { return x; }
	public float getY() { return y; }

	public void set(float x, float y) {
		setX(x);
		setY(y);
	}
	public void set(Vector2f vector) {
		setX(vector.getX());
		setY(vector.getY());
	}

	public Vector2f translate(float x, float y) {
		set(getX() + x, getY() + y);
		return this;
	}

	public Vector2f scale(float factor) {
		set(getX() * factor, getY() * factor);
		return this;
	}

	public Vector2f negate() {//puts the negation of itself in itself
		return negate(this);
	}
	public Vector2f negate(Vector2f dest) {
		if (dest == null) dest = new Vector2f();
		dest.set(-getX(), -getY());
		return dest;
	}
	public Vector2f normalise() {
		return normalise(this);
	}
	public Vector2f normalise(Vector2f dest) {
		float length = length();
		if (dest == null) dest = new Vector2f();
		dest.set(getX()/length, getY()/length);
		return dest;
	}
	public float lengthSquared() {
		return x * x + y * y;
	}
	
	public void store(FloatBuffer buf) {
		buf.put(getX());
		buf.put(getY());
	}
	public void load(FloatBuffer buf) {
		setX(buf.get());
		setY(buf.get());
	}
	
	public String toString() {
		return "[" + getX() + ", " + getY() + "]";
	}

	public static Vector2f add(Vector2f left, Vector2f right, Vector2f dest) {
		if (dest == null) dest = new Vector2f();
		dest.set(left.getX() + right.getX(), left.getY() + right.getY());
		return dest;
	}
	public static Vector2f sub(Vector2f left, Vector2f right, Vector2f dest) {
		if (dest == null) dest = new Vector2f();
		dest.set(left.getX() - right.getX(), left.getY() - right.getY());
		return dest;
	}
	public static float dot(Vector2f left, Vector2f right) {
		return left.getX() * right.getX() + left.getY() * right.getY();
	}
	public static float angle(Vector2f a, Vector2f b) {
		float dls = dot(a, b) / (a.length() * b.length());
		if (dls < -1f) dls = -1f;
		else if (dls > 1f) dls = 1f;
		return (float) Math.acos(dls);
	}
}