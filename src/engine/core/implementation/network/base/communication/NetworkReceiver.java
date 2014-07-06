package engine.core.implementation.network.base.communication;

import engine.core.implementation.network.base.communication.message.Message;

public interface NetworkReceiver {
	public void receiveMessage(Message message);
}
