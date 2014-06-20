package utils.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A buffer that manages a list of add and remove entries with a lock.
 * 
 * @param <T>
 */
public class TwoWayBuffer<T> {
	private List<T> m_addBuffer;
	private List<T> m_removeBuffer;
	private Lock m_lock;

	{
		m_addBuffer = new ArrayList<T>();
		m_removeBuffer = new ArrayList<T>();
		m_lock = new ReentrantLock();
	}

	public TwoWayBuffer() {

	}

	/**
	 * Locks both buffers, preventing new entries from being added.
	 */
	public void lock() {
		m_lock.lock();
	}

	/**
	 * Unlocks both buffers, permitting entries to be added.
	 */
	public void unlock() {
		m_lock.unlock();
	}

	/**
	 * Adds an entry to the add buffer.
	 * 
	 * @param entry
	 */
	public void bufferAdd(T entry) {
		try {
			m_lock.lock();
			m_addBuffer.add(entry);
		} finally {
			m_lock.unlock();
		}
	}

	/**
	 * Adds an entry to the remove buffer.
	 * 
	 * @param entry
	 */
	public void bufferRemove(T entry) {
		try {
			m_lock.lock();
			m_removeBuffer.add(entry);
		} finally {
			m_lock.unlock();
		}
	}

	public List<T> getAddBuffer() {
		return m_addBuffer;
	}

	public List<T> getRemoveBuffer() {
		return m_removeBuffer;
	}

	public void clear() {
		m_addBuffer.clear();
		m_removeBuffer.clear();
	}
}
