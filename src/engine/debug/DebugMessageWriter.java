package engine.debug;

import java.io.IOException;

import engine.core.implementation.network.base.communication.lowlevel.ByteWriterInterpreter;
import engine.core.implementation.network.base.communication.lowlevel.MessageWriter;
import engine.core.implementation.network.base.communication.message.Message;

public class DebugMessageWriter extends MessageWriter {

	public DebugMessageWriter(ByteWriterInterpreter interpreter) {
		super(interpreter);
	}

	@Override
	public void writeMessage(Message message) {
		try {
			super.writeMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.err.println("Writing message " + message.getCommand() + " from Thread "
				+ Thread.currentThread().getName());
		Thread.dumpStack();
	}
}
