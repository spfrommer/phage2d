package engine.inputs.keyboard;

import engine.inputs.InputTrigger;

//For special cases when a -1 value is wanted
public class NegativeKeyTrigger extends InputTrigger implements KeyListener {
	public NegativeKeyTrigger() {}
	public NegativeKeyTrigger(Key key) {
		key.addKeyListener(this);
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		if (event instanceof KeyPressedEvent) {
			trigger(-1f);
		} else if (event instanceof KeyReleasedEvent) {
			trigger(0f);
		}
	}
}
