package engine.graphics.lwjgl.shader;

import java.util.*;

public class InputDictionary {
	private ArrayList<Input> m_inputs = new ArrayList<Input>();
	
	public void addInput(Input input) {
		m_inputs.add(input);
	}
	public void clear() {
		m_inputs.clear();
	}

	public List<Input> getInputs() { return m_inputs; }
}