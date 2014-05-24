package utils.grid;

import java.awt.Color;
import java.awt.Graphics2D;

public class Cell {	
	private Grid m_grid;
	private int m_x;
	private int m_y;
	private int m_size;
	
	private Color m_selectedColor;
	private Color m_fillColor;
	
	public Cell(int x, int y, int size, Grid g) {
		m_x = x;
		m_y = y;
		m_size = size;
		m_grid = g;
	}
	public boolean isSelected() { 
		return !(m_fillColor == null || m_fillColor.getAlpha() == 0); 
	}
	public void setSelected(boolean selected) {
		if (selected) {
			setColor(getSelectedColor());
		} else {
			clearColor();
		}
	}
	public Color getSelectedColor() {
		if (m_selectedColor == null) return m_grid.getSelectedColor();
		else return m_selectedColor;
	}
	public void setSelectedColor(Color color) {
		m_selectedColor = color;
	}
	public void setColor(Color c) {
		m_fillColor = c;
	}
	public Color getColor() { return m_fillColor; }
	
	public void clearColor() {
		m_fillColor = new Color(255, 255, 255, 0);
	}
	public void flip() {
		if (!isSelected()) {
			setSelected(true);
		} else {
			setSelected(false);
		}
	}
	
	public void render(Graphics2D g2d) {
		int pixelX = m_x * m_size;
		int pixelY = m_y * m_size;
		
		if (m_fillColor != null && m_fillColor.getAlpha() != 0) {
			Color old = g2d.getColor();
			g2d.setColor(m_fillColor);
			g2d.fillRect(pixelX, pixelY, m_size, m_size);
			g2d.setColor(old);
		}
	}
}
