package engine.inputs.keyboard;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class JavaKeyListGenerator {
	public static List<Key> createKeyList() {
		ArrayList<Key> keys = new ArrayList<Key>();
		keys.add(new Key(KeyEvent.VK_A, 'A'));
		keys.add(new Key(KeyEvent.VK_B, 'B'));
		keys.add(new Key(KeyEvent.VK_C, 'C'));
		keys.add(new Key(KeyEvent.VK_D, 'D'));
		keys.add(new Key(KeyEvent.VK_E, 'E'));
		keys.add(new Key(KeyEvent.VK_F, 'F'));
		keys.add(new Key(KeyEvent.VK_G, 'G'));
		keys.add(new Key(KeyEvent.VK_H, 'H'));
		keys.add(new Key(KeyEvent.VK_I, 'I'));
		keys.add(new Key(KeyEvent.VK_J, 'J'));
		keys.add(new Key(KeyEvent.VK_K, 'K'));
		keys.add(new Key(KeyEvent.VK_L, 'L'));
		keys.add(new Key(KeyEvent.VK_M, 'M'));
		keys.add(new Key(KeyEvent.VK_N, 'N'));
		keys.add(new Key(KeyEvent.VK_O, 'O'));
		keys.add(new Key(KeyEvent.VK_P, 'P'));
		keys.add(new Key(KeyEvent.VK_Q, 'Q'));
		keys.add(new Key(KeyEvent.VK_R, 'R'));
		keys.add(new Key(KeyEvent.VK_S, 'S'));
		keys.add(new Key(KeyEvent.VK_T, 'T'));
		keys.add(new Key(KeyEvent.VK_U, 'U'));
		keys.add(new Key(KeyEvent.VK_V, 'V'));
		keys.add(new Key(KeyEvent.VK_W, 'W'));
		keys.add(new Key(KeyEvent.VK_X, 'X'));
		keys.add(new Key(KeyEvent.VK_Y, 'Y'));
		keys.add(new Key(KeyEvent.VK_Z, 'Z'));
		keys.add(new Key(KeyEvent.VK_SHIFT, '\0',"Left Shift"));
		return keys;
	}
}
