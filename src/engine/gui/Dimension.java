package engine.gui;

public class Dimension {
	private int m_width;
	private int m_height;
	
	public Dimension() {}
	public Dimension(int w, int h) {
		setWidth(w);
		setHeight(h);
	}
	
	public void setHeight(int h) { m_height = h; }
	public void setWidth(int w) { m_width = w; }
	public int getHeight() { return m_height; }
	public int getWidth() { return m_width; }
	
	public String toString() {
		return "W: " + m_width + " H: " + m_height; 
	}
}
