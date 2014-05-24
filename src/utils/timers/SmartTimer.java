package utils.timers;


import java.awt.event.ActionListener;

import javax.swing.Timer;

public class SmartTimer extends Timer {
	private AbsoluteTimer m_timer = new AbsoluteTimer();

	public SmartTimer(int delay, ActionListener listener) {
		super(delay, listener);
	}

	@Override
	public void start() {
		super.start();
		m_timer.start();
	}

	@Override
	public void restart() {
		super.restart();
		m_timer.start();
	}

	public double getElapsedTime() {
		return m_timer.getTimeMillis();
	}
}
