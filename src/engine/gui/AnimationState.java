package engine.gui;

import java.util.HashMap;

public class AnimationState {
	private HashMap<String, Boolean> m_animationStates = new HashMap<String, Boolean>();

	public void setState(String animation, boolean state) { 
		m_animationStates.put(animation, state);
	}
	
	public boolean hasState(String animation) {
		return m_animationStates.containsKey(animation);
	}
	
	public boolean getState(String animation) {
		if (m_animationStates.containsKey(animation)) return m_animationStates.get(animation);
		else return false;
	}
	
	@Override
	public String toString() {
		return m_animationStates.toString();
	}
}
