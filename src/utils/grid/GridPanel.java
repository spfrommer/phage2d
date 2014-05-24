package utils.grid;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import utils.physics.Vector;

public class GridPanel extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	Grid m_grid;
	Timer m_timer;
	//Variables for drags...
	boolean m_down;
	int m_startingX;
	int m_startingY;
	
	public GridPanel(int width, int height, int cellSize) {
		this (width, height, cellSize, 50);
	}
	public GridPanel(int width, int height, int cellSize, int refreshMillis) {
		m_grid = new Grid(width, height, cellSize);
		addMouseListener(this);
		m_timer = new Timer(refreshMillis, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		startRefresh();
	}
	
	public Grid getGrid() {
		return m_grid;
	}
	public void setGrid(Grid g) {
		m_grid = g;
	}
	public void cleanup() {
		stopRefresh();
	}
	public void startRefresh() {
		m_timer.start();
	}
	public void stopRefresh() {
		m_timer.stop();
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(m_grid.getWidth() * m_grid.getCellSize(), m_grid.getHeight() * m_grid.getCellSize());
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		m_grid.render(g2d);
		g2d.dispose();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (getGrid().inBounds(e.getX(), e.getY())) getGrid().handleClick(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		m_down = true;
		m_startingX = e.getX();
		m_startingY = e.getY();
		if (!getGrid().inBounds(e.getX(), e.getY())) m_down = false;//Forget the drag
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (m_down) {
			int endX = e.getX();
			int endY = e.getY();
			Vector startingCell = m_grid.getCellPosition(new Vector(m_startingX, m_startingY));
			Vector endCell = m_grid.getCellPosition(new Vector(endX, endY));
			if (!startingCell.equals(endCell)) {
				m_grid.handleDrag(m_startingX, m_startingY, endX, endY);
			}
		}
		m_down = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
