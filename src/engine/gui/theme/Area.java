package engine.gui.theme;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import utils.Pair;
import utils.image.ImageUtils;
import engine.graphics.Renderer;
import engine.gui.AnimationState;
import engine.gui.border.BorderFactory;
import engine.gui.border.ImageBorder;
import engine.gui.theme.xml.XMLParsingUtils;

public class Area extends ImageItem {
	private static final Logger logger = LoggerFactory.getLogger(Area.class);
	
	private Inset m_inset;
	private ImageBorder m_border;
	private BufferedImage m_center;
	
	public Area(ImageBorder b, BufferedImage center) {
		m_border = b;
		m_center = center;
	}
	public Area(Area a) {
		super(a);
		setBorder((ImageBorder) a.getBorder().copy());
		setCenter(ImageUtils.cloneImage(a.getCenter()));
	}
	
	public Area() {}
	
	public void setInset(Inset i) { m_inset = i; }
	public Inset getInset() { return m_inset; }
	public void setBorder(ImageBorder border) { m_border = border; }
	public void setCenter(BufferedImage center) { m_center = center; }
	public ImageBorder getBorder() { return m_border; }
	public BufferedImage getCenter() { return m_center; }
	
	@Override
	public void render(Renderer r, AnimationState s, int x, int y) {
		render(r, s, x, y, getWidth(), getHeight());
	}
	@Override
	public void render(Renderer renderer, AnimationState state, int x, int y, int width, int height) {
		if (m_inset != null) {
			width -= m_inset.getRightBorder();
			x += m_inset.getLeftBorder();
			y += m_inset.getTopBorder();
			height -= m_inset.getBottomBorder();
		}
		//These are the values we should use to draw the center image
		int l = m_border.getLeftBorder();
		int t = m_border.getTopBorder();
		int r = m_border.getRightBorder();
		int b = m_border.getBottomBorder();
		int w = width - (l + r);
		int h = height - (t + b);
		
		renderer.drawImage(m_center, l + x, t + y, w, h);
		m_border.render(renderer, x, y, width, height);
	}
	
	public int getWidth() {
		return getBorder().getLeftBorder() + m_center.getWidth() + getBorder().getRightBorder();
	}
	public int getHeight() {
		return getBorder().getTopBorder() + m_center.getHeight() + getBorder().getBottomBorder();
	}
	
	public int getMinWidth() {
		return getBorder().getLeftBorder() + getBorder().getRightBorder();
	}
	public int getMinHeight() {
		return getBorder().getTopBorder() + getBorder().getBottomBorder();
	}
	
	
	public void tint(Color color) {
		logger.warn("Area tint not yet implemented!!!");
	}
	
	@Override
	public String getItemType() {
		return "area";
	}
	
	@Override
	public void load(Theme root, ImagesFile images, Element xmlElement, File file) {
		setName(xmlElement.getAttribute("name"));
		Pair<ImageBorder, BufferedImage> items = BorderFactory.parseImage(images.getImage(), xmlElement.getAttribute("xywh"), 
				xmlElement.getAttribute("splitx"), xmlElement.getAttribute("splity"));
		setBorder(items.getKey());
		setCenter(items.getValue());
	}
	@Override
	public void loadAttribute(String name, String value) {
		if (name.equals("inset")) {
			setInset(XMLParsingUtils.s_parseInset(value));
		}
		if (name.equals("name")) {
			setName(value);
		}
	}
	
	@Override
	public ThemeItem copy() {
		return new Area(this);
	}
}
