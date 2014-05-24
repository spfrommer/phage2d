package utils.grid;

import java.awt.Color;

import javax.swing.JFrame;


public class Tester {
	JFrame m_frame = new JFrame();
	
	public void run(int width, int height) {
		GridSelector selector = new GridSelector(10, 10, 50);
		selector.getGrid().setSelectedColor(new Color(255, 0, 0, 255));
		Grid g = selector.selectUntilDone(1024, 1024);
		System.out.println("Got grid: " + g);
	}
	public static void main(String[] args) {
		new Tester().run(1024, 1024);
	}
}
