package utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteUtils {
	private ByteUtils() {

	}

	public static byte[] toByteArray(int i) {
		return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(i).array();
	}

	public static byte[] toByteArray(int i, int size) {
		return ByteBuffer.allocate(size).order(ByteOrder.BIG_ENDIAN).putInt(i).array();
	}

	public static byte[] toByteArray(double d) {
		return ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putDouble(d).array();
	}

	public static byte[] toByteArray(double d, int size) {
		return ByteBuffer.allocate(size).order(ByteOrder.BIG_ENDIAN).putDouble(d).array();
	}

	public static byte[] toByteArray(String s) {
		return s.getBytes();
	}

	public static int toInt(byte[] bytes) {
		return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
	}

	public static double toDouble(byte[] bytes) {
		return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getDouble();
	}
}
