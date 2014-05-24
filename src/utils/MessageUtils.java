package utils;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MessageUtils {
	public static void displayMessage(String s) {
		JOptionPane.showMessageDialog(new JFrame(), s);
	}

	public static boolean displayConfirm(String s) {
		int option = JOptionPane.showConfirmDialog(new JFrame(), s);
		if (option == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}

	public static void displayWarning(String s) {
		JOptionPane.showMessageDialog(new JFrame(), s, "Warning", JOptionPane.WARNING_MESSAGE);
	}

	public static void displayError(String s) {
		JOptionPane.showMessageDialog(new JFrame(), s, "Error", JOptionPane.ERROR_MESSAGE);
	}
}