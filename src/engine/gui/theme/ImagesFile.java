package engine.gui.theme;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.w3c.dom.Element;

public class ImagesFile extends ThemeItem {
	BufferedImage m_image = null;
	
	public ImagesFile() {}
	public ImagesFile(BufferedImage image) {
		setImage(image);
	}
	public ImagesFile(ImagesFile file) {
		super(file);
		setImage(file.getImage());
	}
	
	public void setImage(BufferedImage image) { m_image = image; }
	public BufferedImage getImage() { return m_image; }
	
	@Override
	public String getItemType() {
		return "images";
	}

	@Override
	public void load(Theme root, ThemeItem parent, Element xmlElement, File file) {
		String imageName = xmlElement.getAttribute("file");
		setName(imageName);
		File imageFile = new File(file.getParent(), imageName);
		try {
			BufferedImage image = ImageIO.read(imageFile);
			setImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void loadAttribute(String name, String attr) {
	
	}
	
	@Override
	public ThemeItem copy() {
		return new ImagesFile(this);
	}

}
