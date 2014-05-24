package engine.gui.theme;

import engine.graphics.Renderer;
import engine.gui.border.Border;

public class Inset implements Border {
	private int m_l, m_r, m_t, m_b;

	public Inset(int l, int r, int t, int b) {
		m_l = l;
		m_r = r;
		m_t = t;
		m_b = b;
	}
	
	@Override
	public int getBottomBorder() { return m_b; }
	@Override
	public int getLeftBorder() { return m_l; }
	@Override
	public int getRightBorder() { return m_r; }
	@Override
	public int getTopBorder() { return m_t; }

	@Override
	public void render(Renderer r, int x, int y, int w, int h) {}
}
