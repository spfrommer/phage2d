package utils.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A buffer that manages a list of entries with a lock.
 * 
 * @param <T>
 */
public class SingularBuffer<T> {
	private List<T> m_buffer;
	private Lock m_lock;

	{
		m_buffer = new ArrayList<T>();
		m_lock = new ReentrantLock();
	}

	public SingularBuffer() {

	}

	/**
	 * Locks the buffer, preventing new entries from being added.
	 */
	public void lock() {
		m_lock.lock();
	}

	/**
	 * Unlocks the buffer, permitting entries to be added.
	 */
	public void unlock() {
		m_lock.unlock();
	}

	/**
	 * Adds an entry to the buffer.
	 * 
	 * @param entry
	 */
	public void buffer(T entry) {
		try {
			m_lock.lock();
			m_buffer.add(entry);
		} finally {
			m_lock.unlock();
		}
	}

	public List<T> getBuffer() {
		return m_buffer;
	}

	public void clear() {
		m_buffer.clear();
	}
}
