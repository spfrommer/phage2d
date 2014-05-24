package engine.core.network.lowlevel;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import engine.core.network.message.Message;
import engine.core.network.message.parameter.MessageParameter;

public class MessageWriter {
	private ByteWriterInterpreter m_interpreter;
	// to make sure messages don't overwrite each other
	private Lock m_lock;

	public MessageWriter(ByteWriterInterpreter interpreter) {
		m_interpreter = interpreter;
		m_lock = new ReentrantLock();
	}

	public void writeMessage(Message message) throws IOException {
		// m_lock.lock();
		// Thread.dumpStack();
		// System.out.println(Thread.currentThread().getName() + ":" + message.getCommand());
		try {
			m_interpreter.sendCommand(message.getCommand());

			for (MessageParameter mp : message.getParameters()) {
				m_interpreter.sendParameter(mp);
			}

			m_interpreter.sendParameter(new MessageParameter());
		} finally {
			// m_lock.unlock();
		}
	}
}
