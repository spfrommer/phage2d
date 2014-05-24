package engine.core.network.lowlevel;

import java.io.IOException;

import engine.core.network.message.command.CommandInterpreter;
import engine.core.network.message.parameter.MessageParameter;
import engine.core.network.message.parameter.ParameterType;

public class ByteWriterInterpreter {
	private ByteWriter m_writer;
	private CommandInterpreter m_interpreter;

	public ByteWriterInterpreter(ByteWriter writer, CommandInterpreter interpreter) {
		m_writer = writer;
		m_interpreter = interpreter;
	}

	public void sendCommand(String command) throws IOException {
		// if (m_interpreter.getID(command) == 10)
		// System.out.println("Writing command int 10");
		// System.out.println("Writing command int: " +
		// m_interpreter.getID(command));
		m_writer.writeInt(m_interpreter.getID(command));
		m_writer.flush();
	}

	public void sendParameter(MessageParameter parameter) throws IOException {
		m_writer.writeInt(parameter.getType().intValue());

		if (parameter.getType() == ParameterType.INT) {
			m_writer.writeInt(parameter.getIntValue());
		}
		if (parameter.getType() == ParameterType.DOUBLE) {
			m_writer.writeDouble(parameter.getDoubleValue());
		}
		if (parameter.getType() == ParameterType.STRING) {
			m_writer.writeString(parameter.getStringValue());
		}
	}
}
