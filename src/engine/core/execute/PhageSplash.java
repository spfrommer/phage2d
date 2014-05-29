package engine.core.execute;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import utils.image.ImageUtils;

/**
 * Shows the splash screen on startup.
 */
public class PhageSplash extends JFrame {
	public PhageSplash() {
		// this.setUndecorated(true);
		this.setLayout(null);
		this.setSize(350, 300);
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(new Color(250, 250, 250));

		JLabel phageLogo = new JLabel(new ImageIcon(ImageUtils.scaleImage(
				ImageUtils.readImage("/images/phage2dlogo.png"), 140, 200)));
		phageLogo.setBounds(35, 20, 140, 200);
		this.add(phageLogo);

		JLabel lwjglLogo = new JLabel(new ImageIcon(ImageUtils.scaleImage(
				ImageUtils.readImage("/images/lwjgllogo.png"), 100, 50)));
		lwjglLogo.setBounds(215, 20, 100, 50);
		this.add(lwjglLogo);

		JLabel dyn4jLogo = new JLabel(new ImageIcon(ImageUtils.scaleImage(
				ImageUtils.readImage("/images/dyn4jlogo.png"), 100, 50)));
		dyn4jLogo.setBounds(215, 95, 100, 50);
		this.add(dyn4jLogo);

		JLabel slfLogo = new JLabel(new ImageIcon(ImageUtils.scaleImage(ImageUtils.readImage("/images/slflogo.png"),
				100, 50)));
		slfLogo.setBounds(215, 170, 100, 50);
		this.add(slfLogo);

		char copyright = 169;
		JLabel text = new JLabel("Powered by Phage2D Engine " + copyright + " 2014", JLabel.CENTER);
		text.setBounds(35, 215, 280, 50);
		this.add(text);

		this.setVisible(true);
	}

	public static void main(String args[]) {
		PhageSplash test = new PhageSplash();
	}
}
