package engine.gui;

import engine.gui.border.ImageBorder;
import engine.gui.theme.Area;
import engine.gui.theme.Theme;
import engine.gui.theme.xml.XMLLoader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import test.GUITest;

public class ThemeFactory {
	public static Theme loadFromXML(File xml) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		Theme theme = XMLLoader.s_load(new File(GUITest.class.getResource("/themes/basic/chutzpah.xml").toURI()));
		return theme;
	}

	/*public static Theme s_createFromFolder(File file) throws IOException {
		Theme theme = new Theme();
		
		File button = new File(file, "button");
		File frame = new File(file, "frame");
		
		s_createButtonFromFolder(button, theme);
		s_createFrameFromFolder(frame, theme);
		
		return theme;
	}
	public static void s_createButtonFromFolder(File button, Theme theme) throws IOException {
		File normal = new File(button, "normal");
		File pressed = new File(button, "pressed");
		File hover = new File(button, "hover");
		
		Area n = s_createResizableFromFolder(normal);
		Area p = n;
		Area h = n;
		
		if (pressed.exists()) p = s_createResizableFromFolder(pressed);
		if (hover.exists()) h = s_createResizableFromFolder(hover);
		
		theme.put(Button.BUTTON_IMAGE_PROPERTY, n);
		theme.put(Button.PRESSED_BUTTON_IMAGE_PROPERTY, p);
	}
	public static void s_createFrameFromFolder(File frame, Theme theme) throws IOException {
		File normal = new File(frame, "normal");
		File selected = new File(frame, "selected");
		
		s_createFrameStateFromFolder(normal, "normal", theme, 50);
		
		if (selected.exists()) s_createFrameStateFromFolder(selected, "selected", theme, 50);
		else s_createFrameStateFromFolder(normal, "selected", theme, 50);
		theme.put(Frame.FRAME_THEME + ".resize.area.lowerArea", new Dimension(10, 10));
		theme.put(Frame.FRAME_THEME + ".resize.area.left", 10);
		theme.put(Frame.FRAME_THEME + ".resize.area.right", 10);
		theme.put(Frame.FRAME_THEME + ".resize.area.bottom", 10);
	}
	public static void s_createFrameStateFromFolder(File folder, String state, Theme theme, int th) throws IOException {
		File body = new File(folder, "body");
		File bar = new File(folder, "bar");

		Area bodyArea = s_createResizableFromFolder(body);
		Area barArea = s_createResizableFromFolder(bar);
		theme.put(Frame.FRAME_THEME + "." + state + ".top", barArea);
		theme.put(Frame.FRAME_THEME + "." + state + ".top.height", th);
		theme.put(Frame.FRAME_THEME + "." + state + ".body", bodyArea);
	}*/
	public static Area createResizableFromFolder(File folder) throws IOException {
		ImageBorder border = new ImageBorder();
		BufferedImage centerImage = null;

		File center = new File(folder, "center.png");
		File left = new File(folder, "left.png");
		File right = new File(folder, "right.png");
		File bottom = new File(folder, "bottom.png");
		File top = new File(folder, "top.png");

		File tr = new File(folder, "corner_topRight.png");
		File tl = new File(folder, "corner_topLeft.png");
		File br = new File(folder, "corner_bottomRight.png");
		File bl = new File(folder, "corner_bottomLeft.png");

		if (center.exists())
			centerImage = ImageIO.read(center);
		else {
			throw new FileNotFoundException("Could not find center image in: " + folder + " aborting load");
		}

		if (left.exists())
			border.setLeft(ImageIO.read(left));
		if (right.exists())
			border.setRight(ImageIO.read(right));
		if (top.exists())
			border.setTop(ImageIO.read(top));
		if (bottom.exists())
			border.setBottom(ImageIO.read(bottom));

		if (tr.exists())
			border.setCornerTR(ImageIO.read(tr));
		if (tl.exists())
			border.setCornerTL(ImageIO.read(tl));
		if (bl.exists())
			border.setCornerBL(ImageIO.read(bl));
		if (br.exists())
			border.setCornerBR(ImageIO.read(br));

		return new Area(border, centerImage);
	}
}
