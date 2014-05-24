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
		this.setUndecorated(true);
		this.setLayout(null);
		this.setSize(350, 300);
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(new Color(250, 250, 250));

		JLabel phageLogo = new JLabel(new ImageIcon(ImageUtils.scaleImage(
				ImageUtils.readImage("/images/phage2dlogo.png"), 180, 230)));
		phageLogo.setBounds(20, 30, 200, 230);
		this.add(phageLogo);

		JLabel lwjglLogo = new JLabel(new ImageIcon(ImageUtils.scaleImage(
				ImageUtils.readImage("/images/lwjgllogo.png"), 100, 50)));
		lwjglLogo.setBounds(230, 30, 100, 50);
		this.add(lwjglLogo);

		JLabel dyn4jLogo = new JLabel(new ImageIcon(ImageUtils.scaleImage(
				ImageUtils.readImage("/images/dyn4jlogo.png"), 100, 50)));
		dyn4jLogo.setBounds(230, 100, 100, 50);
		this.add(dyn4jLogo);

		JLabel text = new JLabel("Powered by Phage2D Engine © 2014", JLabel.CENTER);
		text.setBounds(35, 250, 280, 50);
		this.add(text);

		this.setVisible(true);
	}

	public static void main(String args[]) {
		PhageSplash test = new PhageSplash();
	}
}
