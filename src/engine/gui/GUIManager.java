package engine.gui;

import engine.graphics.PostProcessor;
import engine.graphics.Renderer;
import engine.inputs.keyboard.KeyEvent;
import engine.inputs.keyboard.KeyListener;
import engine.inputs.keyboard.Keyboard;
import engine.inputs.mouse.Mouse;
import engine.inputs.mouse.MouseEvent;
import engine.inputs.mouse.MouseListener;

import java.util.ArrayList;


public class GUIManager implements PostProcessor, MouseListener, KeyListener {
	private ArrayList<GUI> m_guis = new ArrayList<GUI>();
	private Mouse m_mouse;
	private Keyboard m_keyboard;
	
	public void addGUI(GUI g) {
		g.setMouse(m_mouse);
		g.setKeyboard(m_keyboard);
		m_guis.add(g);
	}
	public void removeGUI(GUI g){
		m_guis.remove(g);
	}
	public ArrayList<GUI> getGUIs() { return m_guis; }
	
	public void setKeyboard(Keyboard k) {
		m_keyboard = k;
		k.addKeyEventListener(this);
		for (GUI gui : m_guis) {
			gui.setKeyboard(k);
		}
	}
	public void setMouse(Mouse m) {
		m_mouse = m;
		m.addMouseListener(this);
		for (GUI gui : m_guis) {
			gui.setMouse(m);
		}
	}
	
	@Override
	public void run(Renderer renderer) {
		for (GUI g : m_guis) {
			g.render(renderer);
		}
	}
	@Override
	public void mouseEvent(MouseEvent e) {
		for (GUI gui : m_guis) {
			gui.onInputEvent(e);
		}
	}
	@Override
	public void onKeyEvent(KeyEvent e) {
		for (GUI gui : m_guis) {
			gui.onInputEvent(e);
		}
	}
}
