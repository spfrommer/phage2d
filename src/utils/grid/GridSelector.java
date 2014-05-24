package utils.grid;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GridSelector implements ActionListener {
	private ArrayList<ActionListener> m_listeners = new ArrayList<ActionListener>();
	
	private JFrame m_frame = new JFrame();
	private GridPanel m_panel;
	private Object m_wait = new Object();
	
	public GridSelector(int width, int height, int cellSize) {
		m_panel = new GridPanel(width, height, cellSize);
	}
	
	public Grid getGrid() { 
		return m_panel.getGrid();
	}
	public void setGrid(Grid g) {
		m_panel.setGrid(g);
	}

	public void select(int frameWidth, int frameHeight) {
		synchronized(m_frame) {
			m_panel.repaint();
			JPanel container = new JPanel();
			container.setLayout(new BorderLayout());
			JScrollPane scroller = new JScrollPane(m_panel);
			scroller.getVerticalScrollBar().setUnitIncrement(13);
			container.add(scroller, BorderLayout.CENTER);
			JButton done = new JButton("Done");
			done.addActionListener(this);
			container.add(done, BorderLayout.SOUTH);
			m_frame.getContentPane().add(container, BorderLayout.CENTER);
			m_frame.setSize(frameWidth, frameHeight);
			m_frame.setVisible(true);
		}
	}
	public Grid selectUntilDone(int width, int height) {
		synchronized(m_wait) {
			select(width, height);
			ActionListener listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					synchronized(m_wait) {
						m_wait.notifyAll();
					}
				}
			};
			addActionListener(listener);
			try {
				m_wait.wait();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			removeActionListener(listener);
			return getGrid();
		}
	}
	public void addActionListener(ActionListener a) {
		m_listeners.add(a);
	}
	public void removeActionListener(ActionListener a) {
		m_listeners.remove(a);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		m_frame.dispose();//Get rid of the frame
		synchronized(m_listeners) {
			for (ActionListener listener : m_listeners) {
				listener.actionPerformed(new ActionEvent(getGrid(), 0, "done"));
			}
		}
	}
}
