package engine.inputs.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JComponent;

public class JavaKeyboard extends Keyboard {
	public JavaKeyboard(JComponent listen) {
		addAll(JavaKeyListGenerator.createKeyList());
		listen.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO: Ignoring for now
			}
			@Override
			public void keyPressed(KeyEvent e) {
				getKey(e.getKeyCode()).setPressed(true);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				getKey(e.getKeyCode()).setPressed(false);
			}
		});
	}
}
