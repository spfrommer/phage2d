package engine.graphics.lwjgl.twl;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.Renderer;
import engine.graphics.PostProcessor;

public class TWL implements PostProcessor {
	private ArrayList<GUI> m_guis = new ArrayList<GUI>();
	private Renderer m_renderer;
	
	public TWL(Renderer r) {
		m_renderer = r;
	}
	
	@Override
	public void run(engine.graphics.Renderer renderer) {
		AffineTransform trans = renderer.getTransform();
		for (GUI gui : m_guis) {
			gui.update();
		}
		renderer.setTransform(trans);
	}
	
	public Renderer getRenderer() {
		return m_renderer;
	}
	public void addGUI(GUI gui) {
		m_guis.add(gui);
	}
	public void removeGUI(GUI gui) {
		m_guis.remove(gui);
	}
	public ArrayList<GUI> getGUIs() {
		return m_guis;
	}

}
