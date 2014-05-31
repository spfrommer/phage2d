package engine.core.network;

import engine.core.network.message.Message;
import engine.inputs.InputTrigger;

public class NetworkInputTrigger extends InputTrigger implements NetworkReceiver {
	// Id of object to listen
	private int m_id;
	// trigger if this input is received
	private String m_inputListen;

	public NetworkInputTrigger(int id, String inputListen) {
		m_id = id;
		m_inputListen = inputListen;
	}

	@Override
	public void receiveMessage(Message message) {
		if (message.getParameters()[0].getIntValue() == m_id && message.getCommand().equals(m_inputListen)) {
			this.trigger((float) message.getParameters()[1].getDoubleValue());
		}
	}
}
