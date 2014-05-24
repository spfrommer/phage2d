package engine.core.network.lowlevel;

import java.io.IOException;

import engine.core.network.message.command.CommandInterpreter;
import engine.core.network.message.parameter.MessageParameter;
import engine.core.network.message.parameter.ParameterType;

public class ByteReaderInterpreter {
	private ByteReader m_reader;
	private CommandInterpreter m_interpreter;

	public ByteReaderInterpreter(ByteReader reader, CommandInterpreter interpreter) {
		m_reader = reader;
		m_interpreter = interpreter;
	}

	public String readCommand() throws IOException {
		int i = m_reader.readInt();
		return m_interpreter.getCommand(i);
	}

	public MessageParameter readParameter() throws IOException, RuntimeException {
		int dataType = m_reader.readInt();

		if (dataType == ParameterType.INT.intValue()) {
			return (new MessageParameter(m_reader.readInt()));
		}
		if (dataType == ParameterType.DOUBLE.intValue()) {
			return (new MessageParameter(m_reader.readDouble()));
		}
		if (dataType == ParameterType.STRING.intValue()) {
			return (new MessageParameter(m_reader.readString()));
		}
		if (dataType == ParameterType.NULL.intValue()) {
			return (new MessageParameter());
		}

		throw new RuntimeException("Misread data type: " + dataType);
	}
}
