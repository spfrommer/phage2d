package engine.inputs.keyboard;

import engine.inputs.InputTrigger;

public class KeyTrigger extends InputTrigger implements KeyListener {
	public KeyTrigger() {}
	public KeyTrigger(Key key) {
		key.addKeyListener(this);
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		if (event instanceof KeyPressedEvent) {
			trigger(1f);
		} else if (event instanceof KeyReleasedEvent) {
			trigger(0f);
		}
	}
}
