package engine.graphics.lwjgl;

import java.awt.geom.AffineTransform;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import engine.graphics.lwjgl.vector.Matrix4f;

public class LWJGLUtils {
    public static final int SIZE_FLOAT = 4;  // four bytes in a float

    public static FloatBuffer allocFloats(int howmany) {
        return ByteBuffer.allocateDirect(howmany * SIZE_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public static FloatBuffer allocFloats(float[] floatarray) {
        FloatBuffer fb = ByteBuffer.allocateDirect(floatarray.length * SIZE_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb.put(floatarray).flip();
        return fb;
    }
    
	public static IntBuffer allocInts(int[] ints) {
        IntBuffer ib = BufferUtils.createIntBuffer(ints.length);
        ib.put(ints).flip();
        return ib;
	}
	public static Matrix4f toMatrix(AffineTransform Tx) {
		double[] mat3 = new double[9];
		Tx.getMatrix(mat3);
		mat3[8] = 1;

		float[] mat4Column = {
				(float) mat3[0], (float) mat3[2], 0, (float) mat3[4],
				(float) mat3[1], (float) mat3[3], 0, (float) mat3[5],
				      		  0,			    0, 1, 				 0,
				(float) mat3[6], (float) mat3[7], 0, (float) mat3[8]
		};
		Matrix4f mat4 = new Matrix4f();
		mat4.loadTranspose(FloatBuffer.wrap(mat4Column));
		return mat4;
	}
	public static FloatBuffer toBuffer(Matrix4f mat) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		mat.store(buffer);
		buffer.flip();
		return buffer;
	}
}
