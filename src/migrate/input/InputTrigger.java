package migrate.input;

import java.util.ArrayList;

public class InputTrigger {
	private ArrayList<TriggerListener> m_listeners = new ArrayList<TriggerListener>();
	
	public void addTriggerListener(TriggerListener l) {
		m_listeners.add(l);
	}
	public void removeTriggerListener(TriggerListener l) {
		m_listeners.remove(l);
	}
	public void trigger(float value) {
		for (TriggerListener l : m_listeners) {
			l.onTrigger(this, value);
		}
	}
	public interface TriggerListener {
		public void onTrigger(InputTrigger trigger, float value);
	}
}
