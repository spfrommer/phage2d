package utils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImageDisplay {
	private Image m_image;
	private JFrame m_frame = new JFrame("Display");
	private ImagePanel m_panel = new ImagePanel();
	
	{
		m_frame.getContentPane().add(m_panel);
		m_frame.setSize(new Dimension(100, 100));
	}
	
	public void setImage(Image i) {
		m_image = i;
	}
	public Image getImage() {
		return m_image;
	}
	public void show() {
		m_panel.repaint();
		m_frame.pack();
		m_frame.setVisible(true);
	}
	public void hide() {
		m_frame.setVisible(false);
	}
	public void popup() {
		final Object popupWait = new Object();
		m_frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				synchronized(popupWait) {
					popupWait.notifyAll();
				}
			}
		});
		show();
		synchronized(popupWait) {
			try {
				popupWait.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		m_frame.setVisible(false);
		m_frame.dispose();
	}
			
	public class ImagePanel extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(m_image, 0, 0, getWidth(), getHeight(), null);
		}
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(m_image.getWidth(null), m_image.getHeight(null));
		}
	}
	public static void main(String[] args) throws MalformedURLException, IOException, URISyntaxException {
		BufferedImage img = ImageIO.read(ImageDisplay.class.getResource("/images/bullet1.png").toURI().toURL());
		ImageDisplay display = new ImageDisplay();
		display.setImage(img);
		display.popup();
	}
}
