package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import utils.timers.AbsoluteTimer;
import engine.core.implementation.camera.base.Camera;
import engine.core.implementation.camera.base.Display;
import engine.core.implementation.camera.base.SingleViewPortLayout;
import engine.core.implementation.camera.base.ViewPort;
import engine.graphics.font.BMFont;
import engine.graphics.font.BMFontXMLLoader;
import engine.graphics.font.Font;
import engine.graphics.lwjgl.LWJGLDisplay;
import engine.graphics.lwjgl.LWJGLKeyboard;
import engine.graphics.lwjgl.LWJGLMouse;
import engine.graphics.lwjgl.LWJGLRenderer;
import engine.gui.Button;
import engine.gui.Console;
import engine.gui.Dimension;
import engine.gui.FocusManager;
import engine.gui.Frame;
import engine.gui.GUI;
import engine.gui.GUIManager;
import engine.gui.Label;
import engine.gui.Panel;
import engine.gui.layout.BorderLayout;
import engine.gui.theme.Theme;
import engine.gui.theme.xml.XMLLoader;
import engine.inputs.keyboard.Keyboard;
import engine.inputs.mouse.Mouse;

public class GUITest {
	private static Logger logger = LoggerFactory.getLogger(GUITest.class);

	private static int m_refreshTime;

	public static void main(String[] args) throws URISyntaxException, IOException, ParserConfigurationException,
			SAXException {
		Display display = new LWJGLDisplay(1024, 1024);

		display.init();

		SingleViewPortLayout layout = new SingleViewPortLayout(display);
		ViewPort fake = new ViewPort(new Camera());
		layout.setViewPort(fake);

		fake.resized(1024, 1024);

		layout.validate();

		Mouse mouse = new LWJGLMouse(fake.getViewShape());
		Keyboard keyboard = LWJGLKeyboard.instance();

		GUIManager manager = new GUIManager();
		// Set keyboard/mouse
		manager.setMouse(mouse);
		manager.setKeyboard(keyboard);

		Font font = null;
		try {
			File fontFile = new File(FontTest.class.getResource("/themes/basic/ptsans.fnt").toURI());
			List<BMFont> fonts = BMFontXMLLoader.loadFonts(fontFile);
			font = fonts.get(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		Theme theme = XMLLoader.s_load(new File(GUITest.class.getResource("/themes/blue/guiTheme.xml").toURI()));

		logger.debug("Using theme: " + theme);

		FocusManager fManager = new FocusManager();

		GUI gui = new GUI();
		gui.setTheme(theme);

		Button b = new Button();
		b.setWidget(new Label("CENTER", font));
		b.setSize(new Dimension(100, 100));
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info("CENTER Pressed");
			}
		});
		Frame frame = new Frame();
		frame.setSize(new Dimension(200, 200));

		Panel panel = frame.getPanel();
		panel.setLayout(new BorderLayout());
		panel.add(b, BorderLayout.CENTER);
		panel.add(new Button(new Label("EAST", font)), BorderLayout.EAST);
		panel.add(new Button(new Label("WEST", font)), BorderLayout.WEST);
		panel.add(new Button(new Label("NORTH", font)), BorderLayout.NORTH);
		panel.add(new Button(new Label("SOUTH", font)), BorderLayout.SOUTH);
		gui.add(frame);
		gui.setFocusManager(fManager);

		manager.addGUI(gui);

		panel.validate();
		gui.validate();
		logger.debug("Panel absolute position: " + panel.getAbsoluteBounds());
		logger.debug("CENTER button absolute position: " + b.getAbsoluteBounds());
		logger.debug("CENTER button local bounds: " + b.getBounds());

		Console console = new Console(font);
		manager.addGUI(console);

		console.start();

		logger.debug("Done setting up gui!");

		AbsoluteTimer totalLoopTimer = new AbsoluteTimer();
		AbsoluteTimer actionTimer = new AbsoluteTimer();
		long lastTimeNanos = 100;
		long lastActionTimeNanos = 0;

		logger.debug("Going into render loop");

		while (!org.lwjgl.opengl.Display.isCloseRequested()) {
			manager.run(LWJGLRenderer.instance());
			LWJGLRenderer.instance().update(60);
		}
	}

	public static File getFile(File dir) {
		JFileChooser fChoose = new JFileChooser(dir);
		fChoose.showOpenDialog(null);
		File myFile = fChoose.getSelectedFile();
		return myFile;
	}
}
