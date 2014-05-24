package engine.core.network.message;

public class MessageDeclaration {
	private String m_command;

	// private int m_numParameters;

	public MessageDeclaration(String command/*, int numParameters*/) {
		m_command = command;
		// m_numParameters = numParameters;
	}

	public String getCommand() {
		return m_command;
	}

	/*public int getNumParameters() {
		return m_numParameters;
	}*/
}
