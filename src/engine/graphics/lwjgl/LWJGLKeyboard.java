package engine.graphics.lwjgl;

import org.lwjgl.LWJGLException;

import engine.inputs.keyboard.Key;
import engine.inputs.keyboard.KeyPressedEvent;
import engine.inputs.keyboard.KeyReleasedEvent;
import engine.inputs.keyboard.KeyTypedEvent;
import engine.inputs.keyboard.Keyboard;

public class LWJGLKeyboard extends Keyboard {
	private static LWJGLKeyboard INSTANCE;

	public EventThread m_eventThread = new EventThread();

	private LWJGLKeyboard() {
		m_eventThread = new EventThread();
		Thread thread = new Thread(m_eventThread);
		thread.setName("LWJGLKeyboardEventThread");
		thread.start();
	}

	public static LWJGLKeyboard instance() {
		if (INSTANCE == null) {
			INSTANCE = new LWJGLKeyboard();
		}
		return INSTANCE;
	}

	public static void createLWJGLKeyboard() {
		if (!org.lwjgl.input.Keyboard.isCreated()) {
			try {
				org.lwjgl.input.Keyboard.create();
				org.lwjgl.input.Keyboard.enableRepeatEvents(false);
			} catch (LWJGLException e) {
				throw new RuntimeException("Cannot get LWJGL Keyboard before display has been initialized");
			}
		}
	}

	public class EventThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				/*
				if (org.lwjgl.input.Keyboard.isCreated()) {
					if (org.lwjgl.input.Keyboard.next()) {
						int id = org.lwjgl.input.Keyboard.getEventKey();
						boolean state = org.lwjgl.input.Keyboard.getEventKeyState();
						Key key = getKey(id);
						if (key == null) System.out.println("Could not find key with ID: " + id);
						if (state == true) {
							event(new KeyPressedEvent(key));
							event(new KeyTypedEvent(key));
						} else {
							event(new KeyReleasedEvent(key));
						}
					}
				}*/
				if (org.lwjgl.input.Keyboard.isCreated()) {
					org.lwjgl.input.Keyboard.poll();
					for (Key key : getKeys()) {
						if (key.getID() < org.lwjgl.input.Keyboard.KEYBOARD_SIZE)
							poll(key);
					}
				} else if (org.lwjgl.opengl.Display.isCreated()) {
					createLWJGLKeyboard();
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void poll(Key key) {
		int id = key.getID();

		boolean state = org.lwjgl.input.Keyboard.isKeyDown(id);
		// Key shifted = getLayout().getShiftMapping().getShiftedKey(key, outer());
		// if (shifted == null) {
		// System.out.println("Could not find shifted key for: " + key.getName());
		// shifted = key;
		// }

		/*boolean shift = false;
		if (hasKey("SHIFT")) shift = getKey("SHIFT").isPressed();
		//if shift is on, shift it
		if (shift) actual = key;*/

		if (key.isPressed() != state) {
			if (state) {
				// Key was not pressed, but now is
				key.onKeyEvent(new KeyPressedEvent(key));
				key.onKeyEvent(new KeyTypedEvent(key));
			} else {
				// Was pressed, and now released
				// System.out.println("Key released: " + key.getName());
				key.onKeyEvent(new KeyReleasedEvent(key));
			}
		}
	}

	private LWJGLKeyboard outer() {
		return this;
	}
}
