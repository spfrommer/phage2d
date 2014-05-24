package engine.inputs.keyboard;

import engine.inputs.InputEvent;

public abstract class KeyEvent extends InputEvent {
	//TODO: getKeyboard();
	
	public abstract Key getKey();
	public abstract void setKey(Key key);
	public abstract KeyEvent copy();
}
