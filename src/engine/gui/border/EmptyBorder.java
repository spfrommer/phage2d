package engine.gui.border;

import engine.graphics.Renderer;

public class EmptyBorder implements Border {
	private int m_top;
	private int m_left;
	private int m_bottom;
	private int m_right;
	
	public EmptyBorder(int top, int left, int bottom, int right) {
		init(top, left, bottom, right);
	}
	public EmptyBorder(int horizontal, int vertical) {
		this(vertical, horizontal, vertical, horizontal);
	}
	public EmptyBorder(int border) {
		this(border, border);
	}
	public EmptyBorder(String string) {
		String[] parts = string.split(",");
		if (parts.length == 4) {
			init(Integer.parseInt(parts[0]),
					Integer.parseInt(parts[1]),
					Integer.parseInt(parts[2]),
					Integer.parseInt(parts[3]));
		} else if (parts.length == 2) {
			init(Integer.parseInt(parts[0]),
					Integer.parseInt(parts[1]));
		} else if (parts.length == 1) {
			init(Integer.parseInt(parts[0]));
		} else {
			throw new RuntimeException("Illegal number of elements in border. Must be 4, 2, or 1!");
		}
	}
	
	public void init(int top, int left, int bottom, int right) {
		m_top = top;
		m_left = left;
		m_bottom = bottom;
		m_right = right;
	}
	public void init(int horizontal, int vertical) {
		init(vertical, horizontal, vertical, horizontal);
	}
	public void init(int border) {
		init(border, border);
	}
			
	@Override
	public int getBottomBorder() {
		return m_bottom;
	}
	@Override
	public int getLeftBorder() {
		return m_left;
	}
	@Override
	public int getRightBorder() {
		return m_right;
	}
	@Override
	public int getTopBorder() {
		return m_top;
	}
	
	@Override
	public void render(Renderer r, int x, int y, int w, int h) {}
}
