package migrate.vector;

import java.nio.FloatBuffer;

public class Matrix2f extends Matrix {
	public float m00, m01, m10, m11;
	
	public Matrix2f() {}
	
	public Matrix2f(Matrix2f src) {
		load(src);
	}
	
	public float[][] toFloats() { 
		float[][] floats = {{m00, m01},
				 			{m10, m11}};
		return floats;
	}
	
	public Matrix2f load(Matrix2f src) {
		return load(src, this);
	}
	public Matrix2f load(FloatBuffer buf) {
		return load(buf, this);
	}
	public Matrix2f load(float[][] floats) {
		return load(floats, this);
	}
	public Matrix2f loadTranspose(FloatBuffer buf) {
		return load(buf, null).transpose(this);
	}
	public Matrix2f store(FloatBuffer buf) {
		return store(this, buf);
	}
	public Matrix2f storeTranspose(FloatBuffer buf) {
		return store(transpose(null), buf);
	}
	public Matrix2f setIdentity() {
		return setIdentity(this);
	}
	public Matrix2f setZero() {
		return setZero(this);
	}
	public Matrix2f transpose() {
		return transpose(this, this);
	}
	public Matrix2f transpose(Matrix2f dest) {
		return transpose(this, dest);
	}
	public Matrix2f negate(Matrix2f dest) {
		return negate(this, dest);
	}
	public Matrix2f negate() {
		return negate(this, this);
	}
	public float determinant() {
		return determinant(this);
	}
	public Matrix2f sub(Matrix2f right, Matrix2f dest) {
		return sub(this, right, dest);
	}
	public Matrix2f sub(Matrix2f right) {
		return sub(this, right, this);
	}
	public Matrix2f add(Matrix2f right, Matrix2f dest) {
		return add(this, right, dest);
	}
	public Matrix2f add(Matrix2f right) {
		return add(this, right, this);
	}
	
	public Vector2f transform(Vector2f right, Vector2f dest) {
		return transform(this, right, dest);
	}
	
	public Matrix2f invert() {
		return invert(this);
	}
	public Matrix2f invert(Matrix2f dest) {
		return invert(this, dest);
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(m00).append(' ').append(m10).append(' ').append('\n');
		buf.append(m01).append(' ').append(m11).append(' ').append('\n');
		return buf.toString();
	}
	
	public Matrix2f clone() {
		return new Matrix2f(this);
	}
	
	public static float determinant(Matrix2f mat) {
		return mat.m00 * mat.m11 - mat.m01 * mat.m10;
	}
	public static Matrix2f load(Matrix2f src, Matrix2f dest) {
		if (dest == null) dest = new Matrix2f();
		dest.m00 = src.m00;
		dest.m01 = src.m01;
		dest.m10 = src.m10;
		dest.m11 = src.m11;
		return dest;
	}
	public static Matrix2f load(FloatBuffer buf, Matrix2f dest) {
		if (dest == null) dest = new Matrix2f();
		dest.m00 = buf.get();
		dest.m01 = buf.get();
		dest.m10 = buf.get();
		dest.m11 = buf.get();
		return dest;
	}
	public static Matrix2f load(float[][] floats, Matrix2f dest) {
		if (dest == null) dest = new Matrix2f();
		dest.m00 = floats[0][0];
		dest.m01 = floats[0][1];
		dest.m10 = floats[1][0];
		dest.m11 = floats[1][1];
		return dest;
	}
	public static Matrix2f store(Matrix2f mat, FloatBuffer buffer) {
		buffer.put(mat.m00).put(mat.m01);
		buffer.put(mat.m10).put(mat.m11);
		return mat;
	}
	public static Matrix2f setIdentity(Matrix2f dest) {
		if (dest == null) dest = new Matrix2f();
		dest.m00 = 1.0f; dest.m01 = 0.0f;
		dest.m10 = 0.0f; dest.m11 = 1.0f;
		return dest;
	}
	public static Matrix2f setZero(Matrix2f dest) {
		if (dest == null) dest = new Matrix2f();
		dest.m00 = 0.0f; dest.m01 = 0.0f;
		dest.m10 = 0.0f; dest.m11 = 0.0f;
		return dest;
	}
	public static Matrix2f transpose(Matrix2f src, Matrix2f dest) {
		if (dest == null) dest = new Matrix2f();
		float m00 = src.m00;
		float m01 = src.m10;
		float m10 = src.m01;
		float m11 = src.m11;
		
		dest.m00 = m00;
		dest.m01 = m01;
		dest.m10 = m10;
		dest.m11 = m11;
		return dest;
	}
	public static Matrix2f add(Matrix2f left, Matrix2f right, Matrix2f dest) {
		if (dest == null) dest = new Matrix2f();
		dest.m00 = left.m00 + right.m00;
		dest.m01 = left.m01 + right.m01;
		dest.m10 = left.m10 + right.m10;
		dest.m11 = left.m11 + right.m11;
		return dest;
	}
	public static Matrix2f sub(Matrix2f left, Matrix2f right, Matrix2f dest) {
		if (dest == null) dest = new Matrix2f();
		dest.m00 = left.m00 - right.m00;
		dest.m01 = left.m01 - right.m01;
		dest.m10 = left.m10 - right.m10;
		dest.m11 = left.m11 - right.m11;
		return dest;
	}
	public static Matrix2f mul(Matrix2f left, Matrix2f right, Matrix2f dest) {
		if (dest == null) dest = new Matrix2f();
		float m00 = left.m00 * right.m00 + left.m10 * right.m01;
		float m01 = left.m01 * right.m00 + left.m11 * right.m01;
		float m10 = left.m00 * right.m10 + left.m10 * right.m11;
		float m11 = left.m01 * right.m10 + left.m11 * right.m11;

		dest.m00 = m00;
		dest.m01 = m01;
		dest.m10 = m10;
		dest.m11 = m11;
		return dest;
	}
	public static Vector2f transform(Matrix2f left, Vector2f right, Vector2f dest) {
		if (dest == null) dest = new Vector2f();
		float x = left.m00 * right.x + left.m10 * right.y;
		float y = left.m01 * right.x + left.m11 * right.y;

		dest.x = x;
		dest.y = y;
		return dest;
	}
	public static Matrix2f negate(Matrix2f src, Matrix2f dest) {
		if (dest == null) dest = new Matrix2f();
		dest.m00 = -src.m00;
		dest.m01 = -src.m01;
		dest.m10 = -src.m10;
		dest.m11 = -src.m11;

		return dest;
	}
	public static Matrix2f invert(Matrix2f src, Matrix2f dest) {
		float determinant = determinant(src);
		if (determinant != 0) {
			float determinant_inv = 1f/determinant;
			float t00 =  src.m11 * determinant_inv;
			float t01 = -src.m01 * determinant_inv;
			float t11 =  src.m00 * determinant_inv;
			float t10 = -src.m10 * determinant_inv;

			dest.m00 = t00;
			dest.m01 = t01;
			dest.m10 = t10;
			dest.m11 = t11;
			return dest;
		} else return null;
	}
}
