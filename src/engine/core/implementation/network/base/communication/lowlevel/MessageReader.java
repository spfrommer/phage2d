package engine.core.implementation.network.base.communication.lowlevel;

import java.io.IOException;
import java.util.ArrayList;

import engine.core.implementation.network.base.communication.message.Message;
import engine.core.implementation.network.base.communication.message.parameter.MessageParameter;
import engine.core.implementation.network.base.communication.message.parameter.ParameterType;

public class MessageReader {
	private ByteReaderInterpreter m_byteInterpreter;

	public MessageReader(ByteReaderInterpreter interpreter) {
		m_byteInterpreter = interpreter;
	}

	public Message readMessage() throws IOException {
		String command = m_byteInterpreter.readCommand();

		ArrayList<MessageParameter> parameters = new ArrayList<MessageParameter>();
		MessageParameter parameter = null;
		while ((parameter = errorParamRead(command)).getType() != ParameterType.NULL) {
			parameters.add(parameter);
		}

		return (new Message(command, parameters.toArray(new MessageParameter[parameters.size()])));
	}

	private MessageParameter errorParamRead(String command) {
		MessageParameter param = null;
		try {
			param = m_byteInterpreter.readParameter();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Command: " + command);
		}
		return param;
	}
}
