package test;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import engine.core.implementation.camera.base.Display;
import engine.graphics.Color;
import engine.graphics.Renderable;
import engine.graphics.Renderer;
import engine.graphics.font.BMFont;
import engine.graphics.font.BMFontXMLLoader;
import engine.graphics.font.JavaFontConverter;
import engine.graphics.lwjgl.LWJGLDisplay;

public class FontTest implements Renderable {
	private static Logger logger = LoggerFactory.getLogger(FontTest.class);

	private BMFont m_font;
	
	public void init() {
		/*try {
			File fontFile = new File(FontTest.class.getResource("/themes/basic/ptsans.fnt").toURI());
			List<BMFont> fonts = BMFontXMLLoader.loadFonts(fontFile);
			m_font = fonts.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		m_font = JavaFontConverter.s_convert(new java.awt.Font("Serif", java.awt.Font.PLAIN, 26));
	}
	
	
	public void render(Renderer r) {
		String text = "The Quick Brown Fox jumped over the fence!???.";
		//r.setColor(new Color(0f, 1f, 1f, 1f));
		r.setFont(m_font);
		r.drawString(text, 10, m_font.getAscent() + 10);
		
		Rectangle bounds = m_font.getBounds(text);
		
		//r.drawRect(10, 10 + (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
		
		//logger.debug("Bounds width: " + bounds.getWidth() + " bounds height: " + bounds.getHeight());
	}
	
	public static void main(String[] args) {
		Display display = new LWJGLDisplay(1024, 1024, true);
		display.init();


		FontTest test = new FontTest();
		test.init();
		
		display.addRenderable(test);
		
		while(!display.destroyRequested()) {
			display.render();
			display.update(60);
		}
		
		display.destroy();
	}
}
