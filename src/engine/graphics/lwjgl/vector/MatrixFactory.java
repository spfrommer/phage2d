package engine.graphics.lwjgl.vector;

public class MatrixFactory {//useful for creating projection matricies
	public static Matrix4f createFrustrumProjection(float left, float right, float bottom, float top, float near, float far) {
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
	public static Matrix4f createInfiniteFrustrumProjection(float left, float right,
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
	public static Matrix4f createOrthographicProjection(float left, float right,
														float top, float bottom, 
														float far, float near) {
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
	public static Matrix4f createTranslationMatrix(Vector3f translation) {
		float[][] m = {{0f, 0f, 0f, translation.getX()},
					   {0f, 0f, 0f, translation.getY()},
					   {0f, 0f, 0f, translation.getZ()},
					   {0f, 0f, 0f, 1f				 }};
		Matrix4f matrix = new Matrix4f(m);
		return matrix;
	}
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
			
	public static Matrix4f createRotationMatrix(float theta, Vector3f axis) {
		float x = axis.getX();
		float y = axis.getY();
		float z = axis.getZ();
		float C = (float) Math.cos(theta);
		float S = (float) Math.sin(theta);
		float iC = 1 - C;
		float iS = 1 - S;
		
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