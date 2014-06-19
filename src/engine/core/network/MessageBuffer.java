package engine.core.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import engine.core.network.lowlevel.MessageWriter;
import engine.core.network.message.Message;

/**
 * Buffers Messages, then flushes them to all its MessageWriters and clears the buffer. Uses a Lock to be thread safe.
 */
public class MessageBuffer {
	private List<Message> m_buffer;
	private List<MessageWriter> m_writers;
	private Lock m_lock;

	{
		m_buffer = new ArrayList<Message>();
		m_writers = new ArrayList<MessageWriter>();
		m_lock = new ReentrantLock();
	}

	public MessageBuffer() {
	}

	public void addWriter(MessageWriter writer) {
		m_lock.lock();
		m_writers.add(writer);
		m_lock.unlock();
	}

	public void removeWriter(MessageWriter writer) {
		m_lock.lock();
		m_writers.remove(writer);
		m_lock.unlock();
	}

	public void bufferMessage(Message message) {
		m_lock.lock();
		m_buffer.add(message);
		m_lock.unlock();
	}

	/**
	 * Writes all the Messages to all the MessageWriters, then clears the buffer.
	 */
	public void flush() {
		/*if (m_buffer.size() != 0)
			System.out.println("Flushing " + m_buffer.size() + " messages");*/
		m_lock.lock();
		for (Message message : m_buffer) {
			for (MessageWriter writer : m_writers) {
				try {
					writer.writeMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		m_buffer.clear();
		m_lock.unlock();
	}
}
