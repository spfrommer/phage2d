package utils.timers;

public class AbsoluteTimer {
	private long m_startTime = (long) -1.0;
	private long m_stopTime = (long) -1.0;

	public AbsoluteTimer() {
		start();
	}

	public void start() {
		m_startTime = System.nanoTime();
	}

	public void stop() {
		m_stopTime = System.nanoTime();
	}

	public long getTimeNanos() {
		if (m_stopTime != -1) {
			checkTime();
			return m_stopTime - m_startTime;
		} else {
			return System.nanoTime() - m_startTime;
		}
	}

	public double getTimeMicros() {
		if (m_stopTime != -1) {
			checkTime();
			return (m_stopTime - m_startTime) / 1000.0;
		} else {
			return (System.nanoTime() - m_startTime) / 1000.0;
		}
	}

	public double getTimeMillis() {
		if (m_stopTime != -1) {
			checkTime();
			return (m_stopTime - m_startTime) / 1000000.0;
		} else {
			return (System.nanoTime() - m_startTime) / 1000000.0;
		}
	}

	public double getTimeSeconds() {
		if (m_stopTime != -1) {
			checkTime();
			return (m_stopTime - m_startTime) / 1000000000.0;
		} else {
			return (System.nanoTime() - m_startTime) / 1000000000.0;
		}
	}

	private void checkTime() {
		if (m_startTime == -1)
			System.out.println("No start time");

		if (m_stopTime == -1)
			System.out.println("No stop time");

		if (m_startTime > m_stopTime)
			System.out.println("You started after you stopped");
	}
}
