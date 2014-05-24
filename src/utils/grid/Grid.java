package utils.grid;

import java.awt.Color;
import java.awt.Graphics2D;

import utils.physics.Vector;

public class Grid {
	Color m_selectedColor = Color.BLUE;
	Cell[][] m_cells;
	int m_width;
	int m_height;
	int m_cellSize;
	
	public Grid(int width, int height, int cellSize) {
		m_cells = new Cell[width][height];
		m_width = width;
		m_height = height;
		m_cellSize = cellSize;
	}
	
	public boolean inBounds(int pixelX, int pixelY) {
		return (pixelX < m_width * m_cellSize && pixelY < m_height * m_cellSize);
	}
	public int getWidth() { return m_width; }
	public int getHeight() { return m_height; }
	public int getCellSize() { return m_cellSize; }
	public Color getSelectedColor() { return m_selectedColor; }
	public void setSelectedColor(Color c) { m_selectedColor = c; }
	/**
	 * x, y from top left
	 * @return
	 */
	public Cell getCell(int x, int y) {
		if (x > m_width || y > m_height) return null;
		Cell c = m_cells[x][y];
		if (c == null) {//We have not yet instantiated the cell
			c = new Cell(x, y, m_cellSize, this);
			m_cells[x][y] = c;
		}
		return c;
	}
	public Vector getCellPosition(Vector pixelPosition) {
		int x = ((int) pixelPosition.getX()/m_cellSize);
		int y = ((int) pixelPosition.getY()/m_cellSize);
		if (x >= m_width) x = m_width - 1;
		if (y >= m_height) y = m_height - 1;
		return new Vector(x, y);
	}
	public void handleClick(int pixelX, int pixelY) {
		Vector grid = getCellPosition(new Vector(pixelX, pixelY));
		Cell c = getCell((int) grid.getX(), (int) grid.getY());
		c.flip();
	}
	public void handleDrag(int x1, int y1, int x2, int y2) {
		Vector starting = getCellPosition(new Vector(x1, y1));
		Vector ending = getCellPosition(new Vector(x2, y2));
		if (starting.getX() > ending.getX()) {
			for (int x = (int) starting.getX(); x >= ending.getX(); x--) {
				if (starting.getY() > ending.getY()) {
					for (int y = (int) starting.getY(); y >= ending.getY(); y--) {
						Cell c = getCell(x, y);
						c.flip();
					}
				} else {
					for (int y = (int) starting.getY(); y <= ending.getY(); y++) {
						Cell c = getCell(x, y);
						c.flip();
					}
				}
			}
		} else {
			for (int x = (int) starting.getX(); x <= ending.getX(); x++) {
				if (starting.getY() > ending.getY()) {
					for (int y = (int) starting.getY(); y >= ending.getY(); y--) {
						Cell c = getCell(x, y);
						c.flip();
					}
				} else {
					for (int y = (int) starting.getY(); y <= ending.getY(); y++) {
						Cell c = getCell(x, y);
						c.flip();
					}
				}
			}
		}
	}
	
	public void render(Graphics2D g2d) {
		renderCells(g2d);
		drawGrid(g2d);
	}
	public void renderCells(Graphics2D g2d) {
		for (int x = 0; x < m_width; x++) {
			for (int y = 0; y < m_height; y++) {
				Cell c = getCell(x, y);
				c.render(g2d);
			}
		}
	}
	/**
	 * Will draw the grid outline
	 * @param g2d
	 */
	public void drawGrid(Graphics2D g2d) {
		int pixelHeight = m_height * m_cellSize;
		int pixelWidth  = m_width * m_cellSize;
		//Loop through all columns
		for (int x = 0; x <= m_width; x++) {
			int pixelX = x * m_cellSize;
			//Draw vertical line
			g2d.drawLine(pixelX, 0, pixelX, pixelHeight);
		}
		//Loop through all rows
		for (int y = 0; y <= m_height; y++) {
			int pixelY = y * m_cellSize;
			//Draw horizontal line
			g2d.drawLine(0, pixelY, pixelWidth, pixelY);
		}
	}
}
