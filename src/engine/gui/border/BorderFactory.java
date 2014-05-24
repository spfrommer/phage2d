package engine.gui.border;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import utils.ImageDisplay;
import utils.Pair;

public class BorderFactory {
	//Parses xywh, splitX, and splitY and creates a resizable
	public static Pair<ImageBorder, BufferedImage> parseImage(BufferedImage image, String xywh, String splitX, String splitY) {
		String[] tokens = xywh.split(","); 
		int x = Integer.parseInt(tokens[0]);
		int y = Integer.parseInt(tokens[1]);
		int w = Integer.parseInt(tokens[2]);
		int h = Integer.parseInt(tokens[3]);
		//System.out.println("X: " + x + " Y: " + y + " W: " + w + " H: " + h + " for: " + xywh);
		BufferedImage section = image.getSubimage(x, y, w, h);
		
		int left = 0;
		int right = 0;
		int top = 0;
		int bottom = 0;

		if (!splitX.equals("") && splitX != null) {
			String[] leftRight = splitX.split(",");
			for (String string : leftRight) {
				if (string.startsWith("L")) {
					left = Integer.parseInt(string.substring(1));
				} else if (string.startsWith("R")) {
					right = Integer.parseInt(string.substring(1));
				}
			}
		}

		if (!splitY.equals("") && splitY != null) {
			String[] topBottom = splitY.split(",");
			for (String string : topBottom) {
				if (string.startsWith("T")) {
					top = Integer.parseInt(string.substring(1));
				} else if (string.startsWith("B")) {
					bottom = Integer.parseInt(string.substring(1));
				}
			}
		}
		ImageBorder border = createImageBorder(section, top, left, right, bottom);
		BufferedImage center = createCenter(section, top, left, right, bottom);
		
		return new Pair<ImageBorder, BufferedImage>(border, center);
	}
	
	public static ImageBorder createImageBorder(BufferedImage image, int t, int l, int r, int b) {
		ImageBorder border = new ImageBorder();
		
		if (t != 0 && l != 0) {
			BufferedImage tl = image.getSubimage(0, 0, l, t);
			border.setCornerTL(tl);
		}
		if (t != 0 && r != 0) {
			BufferedImage tr = image.getSubimage(image.getWidth() - r, 0, r, t);
			border.setCornerTR(tr);
		}
		if (b != 0 && l != 0) {
			BufferedImage bl = image.getSubimage(0, image.getHeight() - b, l, b);
			border.setCornerBL(bl);
		}
		if (b != 0 && r != 0) {
			BufferedImage br = image.getSubimage(image.getWidth() - r, image.getHeight() - b, r, b);
			border.setCornerBR(br);
		}
		if (t != 0) {
			BufferedImage top = image.getSubimage(l, 0, image.getWidth() - (l + r), t);
			border.setTop(top);
		}
		if (l != 0) {
			BufferedImage left = image.getSubimage(0, t, l, image.getHeight() - (t + b));
			border.setLeft(left);
		}
		if (r != 0) {
			BufferedImage right = image.getSubimage(image.getWidth() - r, t, r, image.getHeight() - (t + b));
			border.setRight(right);
		}
		if (b != 0) {
			BufferedImage bottom = image.getSubimage(l, image.getHeight() - b, image.getWidth() - (l + r), b);
			border.setBottom(bottom);
		}
		//BufferedImage center = image.getSubimage(5, 5, 5, 19);
		return border;
	}
	public static BufferedImage createCenter(BufferedImage image, int t, int l, int r, int b) {
		return image.getSubimage(l, t, image.getWidth() - (l + r), image.getHeight() - (t + b));
	}
	public static void main(String[] args) throws Exception {
		BufferedImage image = ImageIO.read(BorderFactory.class.getResource("/themes/basic/gui.png"));
		Pair<ImageBorder, BufferedImage> data = parseImage(image, "0,0,15,29", "L5,R5", "T5,B6");
	}
}
