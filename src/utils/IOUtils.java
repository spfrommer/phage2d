package utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Code copied from apache IOUtils.
 */
public class IOUtils {
	/**
	 * Compares the contents of two Streams to determine if they are equal or not.
	 * <p>
	 * This method buffers the input internally using <code>BufferedInputStream</code> if they are not already buffered.
	 *
	 * @param input1
	 *            the first stream
	 * @param input2
	 *            the second stream
	 * @return true if the content of the streams are equal or they both don't exist, false otherwise
	 * @throws NullPointerException
	 *             if either input is null
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
		if (input1 == input2) {
			return true;
		}
		if (!(input1 instanceof BufferedInputStream)) {
			input1 = new BufferedInputStream(input1);
		}
		if (!(input2 instanceof BufferedInputStream)) {
			input2 = new BufferedInputStream(input2);
		}

		int ch = input1.read();
		while (-1 != ch) {
			final int ch2 = input2.read();
			if (ch != ch2) {
				return false;
			}
			ch = input1.read();
		}

		final int ch2 = input2.read();
		return ch2 == -1;
	}

	/**
	 * Copy bytes from an <code>InputStream</code> to an <code>OutputStream</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
	 * <p>
	 * Large streams (over 2GB) will return a bytes copied value of <code>-1</code> after the copy has completed since
	 * the correct number of bytes cannot be returned as an int. For large streams use the
	 * <code>copyLarge(InputStream, OutputStream)</code> method.
	 * 
	 * @param input
	 *            the <code>InputStream</code> to read from
	 * @param output
	 *            the <code>OutputStream</code> to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ArithmeticException
	 *             if the byte count is too large
	 * @since Commons IO 1.1
	 */
	public static int copy(InputStream input, OutputStream output) throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	/**
	 * Copy bytes from a large (over 2GB) <code>InputStream</code> to an <code>OutputStream</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
	 * 
	 * @param input
	 *            the <code>InputStream</code> to read from
	 * @param output
	 *            the <code>OutputStream</code> to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since Commons IO 1.3
	 */
	public static long copyLarge(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[1024 * 4/* default buffer size*/];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
}
