package migrate.vector;

public class MatrixFactory {//useful for creating projection matricies
	/**
	 * Creates a finite frustum projection with the given values
	 */
	public static Matrix4f s_createFrustumProjection(float left, float right, float bottom, float top, float near, float far) {
		float x = (2 * near) / (right - left);
		float y = (2 * near) / (top - bottom);
		float a = (right + left) / (right - left);
		float b = (top + bottom) / (top - bottom);
		float c = (-(far + near)) / (far - near);
		float d = (-2 * far * near) / (far - near);
		float[][] m = {{x, 0f,  a,  0f},
					   {0f, y,  b,  0f},
					   {0f, 0f, c,  d},
					   {0f, 0f, -1, 0f}};
		Matrix4f matrix = new Matrix4f(m);
		return matrix;
	}
	/**
	 * Creates an infinite frustum projection matrix with the given values
	 * Like createFrustumProjection(), but far = Infinity
	 * WARNING: UNTESTED!!
	 */
	public static Matrix4f createInfiniteFrustumProjection(float left, float right,
															float bottom,
															float top, float near) {
//far = oo
		float x = (float) ((2.0 * near) / (right - left));
		float y = (float) ((2.0 * near) / (top - bottom));
		float a = (float) ((right + left) / (right - left));
		float b = (float) ((top + bottom) / (top - bottom));
		float d = (float) (-2.0 * near);
		float[][] m = {{ x,	0f,   a,  0f},
				   {0f,		y,    b,  0f},
				   {0f,		0f, -1f,  d},
				   {0f,		0f, -1f, 0f}};
		Matrix4f matrix = new Matrix4f(m);
		return matrix;
	}
	/**
	 * Creates a orthographic projection matrix with the given values
	 * 
	 */
	public static Matrix4f createOrthographicProjection(float left, float right,
														float top, float bottom, 
														float near, float far) {
		float x = (2)/(right - left);
		float y = (2)/(top - bottom);
		float z = (-2)/(far - near);
		float a = -((right + left)/(right - left));
		float b = -((top + bottom)/(top - bottom));
		float c = -((far + near)/(far - near));
		float[][] m = {{x, 0f,  0f, a},
					   {0f, y,  0f, b},
					   {0f, 0f, z,  c},
					   {0f, 0f, 0f, 1f}};
		Matrix4f matrix = new Matrix4f(m);
		return matrix;
	}
	/**
	 * Will create a translation matrix given the x y and z coordinates to translate by, given in a vector3f
	 * @param translation the x, y, z vector with the translation
	 */
	public static Matrix4f createTranslationMatrix(Vector3f translation) {
		float[][] m = {{1f, 0f, 0f, translation.getX()},
					   {0f, 1f, 0f, translation.getY()},
					   {0f, 0f, 1f, translation.getZ()},
					   {0f, 0f, 0f, 1f				 }};
		Matrix4f matrix = new Matrix4f(m);
		return matrix;
	}
	/**
	 * Creates a matrix that will scale by the x y and z of the given vector
	 * @param scale the vector that should be used to scale
	 */
	public static  Matrix4f createScaleMatrix(Vector3f scale) {
		float x = scale.getX();
		float y = scale.getY();
		float z = scale.getZ();
		float[][] m = {{x, 0f, 0f,  0f},
					   {0f, y, 0f,  0f},
					   {0f, 0f, z,  0f},
					   {0f, 0f, 0f, 1f}};
		return new Matrix4f(m);
	}
	/**
	 * Creates a rotation matrix around a certain axis that will rotate a point by theta radians
	 * @param theta the rotation in radians
	 * @param axis the axis around which to rotate
	 */
	public static Matrix4f createRotationMatrix(float theta, Vector3f axis) {
		float x = axis.getX();
		float y = axis.getY();
		float z = axis.getZ();
		float C = (float) Math.cos(theta);
		float S = (float) Math.sin(theta);
		float iC = 1 - C;
		//Don't know why this one wasn't used...
		//float iS = 1 - S;
		
		float m00 = x * x + (1 - x * x) * C;
		float m01 = iC * x * y - z * S;
		float m02 = iC * x * z + y * S;
		float m10 = iC * x * y + z * S;
		float m11 = y * y + (1 - y * y) * C;
		float m12 = iC * y * z - x * S;
		float m20 = iC * x * z - y * S;
		float m21 = iC * y * z + x * S;
		float m22 = z * z + (1 - z * z) * C;

		float [][] m = {{m00, m01, m02, 0f},
						{m10, m11, m12, 0f},
						{m20, m21, m22, 0f},
						{0f,  0f,  0f,  1f}};

		return new Matrix4f(m);
	}

}