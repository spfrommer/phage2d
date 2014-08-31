package migrate.input;

import static org.lwjgl.input.Keyboard.create;
import static org.lwjgl.input.Keyboard.getEventKey;
import static org.lwjgl.input.Keyboard.getEventKeyState;
import static org.lwjgl.input.Keyboard.isCreated;
import static org.lwjgl.input.Keyboard.next;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class LWJGLKeyboard extends Keyboard {
	private static LWJGLKeyboard s_instance;
	
	private LWJGLKeyboard() {}
	
	public void poll() {
		if (isCreated()) {
			org.lwjgl.input.Keyboard.poll();
			while (next()) {
				int id = getEventKey();
				boolean state = getEventKeyState();
				updateState(getKey(id), state);
			}
		} else if (Display.isCreated()) {
			try {
				create();
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static LWJGLKeyboard getInstance() {
		if (s_instance == null) s_instance = new LWJGLKeyboard();
		return s_instance;
	}
	
	/*protected static class EventSystem {
		private static EventSystem s_instance;
		private ArrayList<LWJGLKeyboard> m_keyboards = new ArrayList<LWJGLKeyboard>();
		
		private EventSystem() {}
		
		public void register(LWJGLKeyboard keyboard) {
			m_keyboards.add(keyboard);
		}
		
		public void poll() {
			if (isCreated()) {
				poll();
				if (next()) {
					int id = getEventKey();
					boolean state = getEventKeyState();
					for (LWJGLKeyboard keyboard : m_keyboards) {
						keyboard.updateState(keyboard.getKey(id), state);
					}
				}
			} else if (Display.isCreated()) {
				try {
					create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}
		}
		public static EventSystem getInstance() {
			if (s_instance == null) {
				s_instance = new EventSystem();
			}
			return s_instance;
		}
	}*/

	
	/*protected static class EventLoop implements Runnable {
		
		// Poll time in ms
		private static int POLL_TIME = 30;
		private static EventLoop s_instance;
		
		private Thread m_thread = new Thread(this);
		
		private ArrayList<LWJGLKeyboard> m_keyboards = new ArrayList<LWJGLKeyboard>();
		
		private EventLoop() {}
		
		public void start() {
			m_thread.start();
		}
		public void stop() {
			m_thread.interrupt();
		}
		
		public void register(LWJGLKeyboard keyboard) {
			m_keyboards.add(keyboard);
			if (!m_thread.isAlive()) start();
		}
		
		public void run() {
			try {
				synchronized(this) {
					while(true) {
						if(Thread.interrupted()) throw new InterruptedException();
						if (isCreated()) {
							poll();
							if (next()) {
								int id = getEventKey();
								boolean state = getEventKeyState();
								for (LWJGLKeyboard keyboard : m_keyboards) {
									keyboard.updateState(keyboard.getKey(id), state);
								}
							}
						} else if (Display.isCreated()) {
							try {
								create();
							} catch (LWJGLException e) {
								e.printStackTrace();
							}
						}
						Thread.sleep(POLL_TIME);
					}
				}
			} catch (InterruptedException e) {
				//Thread interrupted, exit
			}
		}
		
		public static EventLoop getInstance() {
			if (s_instance == null) {
				s_instance = new EventLoop();
			}
			return s_instance;
		}
	}*/
}
