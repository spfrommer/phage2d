package migrate.gui;

public class Dimension {
	private int m_width;
	private int m_height;
	
	public Dimension() {
		this(0, 0);
	}
	public Dimension(int width, int height) {
		m_width = width;
		m_height = height;
	}
	public int getWidth() { return m_width; }
	public int getHeight() { return m_height; }
}
