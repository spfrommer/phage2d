package engine.graphics.font;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import utils.Pair;
import engine.graphics.font.BMFont.BMGlyph;
import engine.graphics.font.Font.Glyph;

public class BMFontXMLLoader {
	// TODO: Implement

	public static List<BMFont> loadFonts(URL xmlURL) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// Get the DOM Builder
		DocumentBuilder builder = factory.newDocumentBuilder();

		// Load and Parse the XML document
		// document contains the complete XML as a Tree.
		Document document = builder.parse(xmlURL.openStream());

		List<BMFont> fonts = new ArrayList<BMFont>();

		NodeList nodes = document.getElementsByTagName("font");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			if (n instanceof Element) {
				fonts.add(parseFont((Element) n, xmlURL.toString()));
			}
		}

		return fonts;
	}

	public static BMFont parseFont(Element e, String xmlURL) throws IOException {
		BMFont font = new BMFont();

		// TODO: Extract info
		Element info = (Element) e.getElementsByTagName("info").item(0);
		font.setName(info.getAttribute("face"));
		font.setSize(Integer.parseInt(info.getAttribute("size")));
		font.setBold(Integer.parseInt(info.getAttribute("bold")) == 0);
		font.setItalic(Integer.parseInt(info.getAttribute("italic")) == 0);

		Element common = (Element) e.getElementsByTagName("common").item(0);
		font.setAscent(Integer.parseInt(common.getAttribute("base")));
		font.setDescent(Integer.parseInt(common.getAttribute("lineHeight")) - font.getAscent());
		//Parse the pages
		Element pgs = (Element) e.getElementsByTagName("pages").item(0);

		HashMap<Integer, Page> pages = new HashMap<Integer, Page>();

		NodeList pageList = pgs.getElementsByTagName("page");

		for (int i = 0; i < pageList.getLength(); i++) {
			Node n = pageList.item(i);
			Element p = (Element) n;
			Pair<Integer, Page> page = parsePage(p, xmlURL);

			pages.put(page.getKey(), page.getValue());
		}

		// Now parse the chars
		Element chars = (Element) e.getElementsByTagName("chars").item(0);
		NodeList charList = chars.getElementsByTagName("char");

		for (int i = 0; i < charList.getLength(); i++) {
			Node node = charList.item(i);
			Element c = (Element) node;

			BMGlyph glyph = parseChar(font, c, pages);

			font.addGlyph(glyph.getChar(), glyph);
		}

		// Now parse the kernings
		// Who needs kernings?

		return font;
	}

	public static BMGlyph parseChar(Font f, Element c, HashMap<Integer, Page> pages) {
		int id = Integer.parseInt(c.getAttribute("id"));

		int x = Integer.parseInt(c.getAttribute("x"));
		int y = Integer.parseInt(c.getAttribute("y"));
		int width = Integer.parseInt(c.getAttribute("width"));
		int height = Integer.parseInt(c.getAttribute("height"));

		int xoff = Integer.parseInt(c.getAttribute("xoffset"));
		int yoff = Integer.parseInt(c.getAttribute("yoffset"));
		int xadv = Integer.parseInt(c.getAttribute("xadvance"));

		Page page = pages.get(Integer.parseInt(c.getAttribute("page")));

		// TODO: Implement channels?

		BufferedImage image = page.getImage().getSubimage(x, y, width, height);

		char character = (char) id;

		BMGlyph glyph = new BMGlyph(f, character, image, xoff, yoff, xadv);

		return glyph;
	}

	public static Pair<Integer, Page> parsePage(Element p, String xmlURL) throws IOException {
		String imageName = p.getAttribute("file");
		BufferedImage image = ImageIO.read(new URL(parentDir(xmlURL) + imageName));
		int id = Integer.parseInt(p.getAttribute("id"));

		Page page = new Page(id, image);

		return new Pair<Integer, Page>(id, page);
	}

	private static String parentDir(String url) {
		if (url == null || url.equals("/") || url.equals(""))
			return "";

		int lastSlash = url.lastIndexOf('/');

		if (lastSlash >= 0)
			return url.substring(0, lastSlash + 1);

		return "";
	}

	// Page info holder class
	public static class Page {
		private BufferedImage m_image;
		private int m_id;

		public Page(int id, BufferedImage image) {
			m_id = id;
			m_image = image;
		}

		public int getID() {
			return m_id;
		}

		public BufferedImage getImage() {
			return m_image;
		}
	}

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException,
			URISyntaxException {
		URL fontFile = BMFontXMLLoader.class.getResource("/themes/basic/ptsans.fnt");
		List<BMFont> fonts = loadFonts(fontFile);
		
		BMFont font = fonts.get(0);
	}
}
