package engine.core.implementation.behavior.base.decorator;

import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;

/**
 * Runs a child Node until it returns SUCCESS or the ticks passed counter exceeds the ticks allowed, at which point it
 * will return SUCCESS and reset the ticks passed counter. Until this happens, the TimeDecorator will return RUNNING.
 */
public class TimeDecorator extends DecoratorNode {
	private int m_ticksAllowed;
	private int m_ticksPassed;

	/**
	 * Initializes ticks allowed to 5;
	 */
	public TimeDecorator() {
		m_ticksAllowed = 5;
	}

	/**
	 * Makes a new TimeDecorator with the given ticks allowed for the child Node to run.
	 * 
	 * @param ticksAllowed
	 */
	public TimeDecorator(int ticksAllowed) {
		m_ticksAllowed = ticksAllowed;
	}

	/**
	 * Makes a new TimeDecorator with the child and how long it is allowed to run.
	 * 
	 * @param ticksAllowed
	 * @param child
	 */
	public TimeDecorator(int ticksAllowed, Node child) {
		super(child);
		m_ticksAllowed = ticksAllowed;
	}

	/**
	 * Sets the ticks that the child Node is allowed to run for.
	 * 
	 * @param ticksAllowed
	 */
	public void setTicksAllowed(int ticksAllowed) {
		m_ticksAllowed = ticksAllowed;
	}

	/**
	 * Returns the ticks that the child Node is allowed to run for.
	 * 
	 * @return
	 */
	public int getTicksAllowed() {
		return m_ticksAllowed;
	}

	@Override
	public Node copy() {
		return new TimeDecorator(m_ticksAllowed, this.getChild().copy());
	}

	@Override
	public ExecutionState update(int ticks) {
		ExecutionState state = this.getChild().update(ticks);
		switch (state) {
		case SUCCESS: {
			m_ticksPassed = 0;
			return ExecutionState.SUCCESS;
		}
		case FAILURE: {
			m_ticksPassed = 0;
			return ExecutionState.FAILURE;
		}
		case RUNNING: {
			m_ticksPassed += ticks;
			if (m_ticksPassed >= m_ticksAllowed) {
				m_ticksPassed = 0;
				return ExecutionState.SUCCESS;
			}

			return ExecutionState.RUNNING;
		}
		default: {
			System.err.println("TimeDecorator should not reach here!");
			return null;
		}
		}
	}
}
