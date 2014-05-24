package engine.inputs;

import java.util.ArrayList;
import java.util.HashMap;

public class InputManager implements TriggerListener {
	public HashMap<InputTrigger, String> m_bindings = new HashMap<InputTrigger, String>();
	public HashMap<String, ArrayList<BindingListener>> m_listeners = new HashMap<String, ArrayList<BindingListener>>();
	public HashMap<String, Float> m_values = new HashMap<String, Float>();
	
	public void addBinding(String binding, InputTrigger trigger) {
		trigger.addTriggerListener(this);
		m_bindings.put(trigger, binding);
	}
	public void removeBinding(InputTrigger trigger) {
		trigger.removeTriggerListener(this);
		m_bindings.remove(trigger);
	}
	
	public void addBindingListener(String binding, BindingListener listener) {
		if (!m_listeners.containsKey(binding)) {
			ArrayList<BindingListener> listeners = new ArrayList<BindingListener>();
			listeners.add(listener);
			m_listeners.put(binding, listeners);
		} else {
			m_listeners.get(binding).add(listener);
		}
	}
	
	public float getValue(String binding) {
		if (m_values.containsKey(binding)) return m_values.get(binding);
		else return 0;
	}
	
	public void removeBindingListener(String binding, BindingListener listener) {
		m_listeners.get(binding).remove(listener);
	}
	
	@Override
	public void onTrigger(InputTrigger trigger, float value) {
		String binding = m_bindings.get(trigger);
		m_values.put(binding, value);
		if (m_listeners.containsKey(binding)) {
			ArrayList<BindingListener> listeners = m_listeners.get(binding);
			for (BindingListener listener : listeners) {
				listener.onAction(binding, value);
			}
		}
	}
}
