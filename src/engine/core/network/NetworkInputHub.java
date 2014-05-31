package engine.core.network;

import java.util.ArrayList;

import engine.core.network.message.Message;

/**
 * Makes a way for NetworkReceivers (network input triggers for user events) to receive messages
 */
public class NetworkInputHub {
	private ArrayList<NetworkReceiver> m_receivers;

	{
		m_receivers = new ArrayList<NetworkReceiver>();
	}

	public NetworkInputHub() {
	}

	public void addNetworkReceiver(NetworkReceiver receiver) {
		m_receivers.add(receiver);
	}

	public void broadcast(Message message) {
		for (NetworkReceiver receiver : m_receivers)
			receiver.receiveMessage(message);
	}
}
