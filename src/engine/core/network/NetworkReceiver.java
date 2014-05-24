package engine.core.network;

import engine.core.network.message.Message;

public interface NetworkReceiver {
	public void receiveMessage(Message message);
}
