package engine.core.implementation.network.base.communication.message;

import engine.core.implementation.network.base.communication.message.parameter.MessageParameter;

public class Message {
	private String m_command;
	private MessageParameter[] m_parameters;

	public Message(String command, MessageParameter[] parameters) {
		m_command = command;
		m_parameters = parameters;
	}

	public String getCommand() {
		return m_command;
	}

	public MessageParameter[] getParameters() {
		return m_parameters;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(m_command + " - ");
		for (MessageParameter mp : m_parameters) {
			builder.append(mp.toString() + " ");
		}

		return builder.toString();
	}
}
