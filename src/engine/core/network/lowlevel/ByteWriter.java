package engine.core.network.lowlevel;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ByteWriter {
	private DataOutputStream m_outputStream;

	public ByteWriter(OutputStream outputStream) {
		m_outputStream = new DataOutputStream(outputStream);
	}

	public void writeInt(int i) throws IOException {
		m_outputStream.writeInt(i);
	}

	public void writeDouble(double d) throws IOException {
		m_outputStream.writeDouble(d);
	}

	public void writeString(String s) throws IOException {
		m_outputStream.writeUTF(s);
	}

	/*public void writeInt(int i) {
		byte[] bytes = ByteUtils.toByteArray(i);
		try {
			m_outputStream.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		flush();
	}

	public void writeDouble(double d) {
		byte[] bytes = ByteUtils.toByteArray(d);
		try {
			m_outputStream.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		flush();
	}

	public void writeString(String s) {
		try {
			writeInt(s.length());
			m_outputStream.write(s.getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		flush();
	}*/

	public void flush() {
		try {
			m_outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
