package engine.core.network.lowlevel;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteReader {
	private DataInputStream m_reader;

	public ByteReader(InputStream reader) {
		m_reader = new DataInputStream(reader);
	}

	public int readInt() throws IOException {
		return m_reader.readInt();
	}

	public double readDouble() throws IOException {
		return m_reader.readDouble();
	}

	public String readString() throws IOException {
		return m_reader.readUTF();
	}

	/*public int readInt() throws IOException {
		byte[] bytes = new byte[4];
		m_reader.read(bytes);
		return ByteUtils.toInt(bytes);
	}

	public double readDouble() throws IOException {
		byte[] bytes = new byte[8];
		m_reader.read(bytes);
		return ByteUtils.toDouble(bytes);
	}

	public String readString() throws IOException {
		int length = readInt();
		byte[] bytes = new byte[length];
		try {
			m_reader.read(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String readString = new String(bytes);
		return readString;
	}*/
}
