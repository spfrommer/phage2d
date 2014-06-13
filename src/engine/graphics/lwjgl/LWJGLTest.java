package engine.graphics.lwjgl;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import utils.image.ImageUtils;
import engine.core.implementation.camera.base.ViewPort;
import engine.graphics.Color;
import engine.graphics.Renderable;
import engine.graphics.Renderer;
import engine.graphics.lwjgl.shader.FragmentShader;
import engine.graphics.lwjgl.shader.Program;
import engine.graphics.lwjgl.shader.Shader.ShaderCompileException;
import engine.graphics.lwjgl.shader.VertexShader;
import engine.graphics.lwjgl.vector.Vector3f;
import engine.inputs.mouse.Mouse;

public class LWJGLTest {
	private static BufferedImage TANK_BODY = ImageUtils.readImage("/images/chassis1.png");
	// private static BufferedImage TANK_GUN = ImageUtils.getImage("/images/game/tankbasicgun.png");
	private static Font FONT;
	private Mouse m_mouse;

	private Program m_light;

	/*	static {
			JFontChooser chooser = new JFontChooser();
			chooser.showDialog(null);
			FONT = chooser.getSelectedFont();
		}
	*/
	public void run(boolean lwjgl) {
		LWJGLRenderer.initDisplayWithoutCanvas(1024, 1024);

		LWJGLRenderer renderer = LWJGLRenderer.instance();

		ViewPort fakePort = new ViewPort(null);
		fakePort.resized(1024, 1024);
		m_mouse = new LWJGLMouse(fakePort.getViewShape());

		m_light = new Program();
		FragmentShader lFrag = new FragmentShader();
		VertexShader lVert = new VertexShader();
		try {
			lFrag.setSource(new File(LWJGLTest.class.getResource("/shaders/lighting.frag").toURI()));
			lFrag.compile();
			lVert.setSource(new File(LWJGLTest.class.getResource("/shaders/lighting.vert").toURI()));
			lVert.compile();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ShaderCompileException e) {
			e.printStackTrace();
		}
		m_light.attachShader(lFrag);
		m_light.attachShader(lVert);
		m_light.link();
		m_light.validate();

		while (!Display.isCloseRequested()) {
			GL11.glDepthMask(false);
			render(renderer, true);
			GL11.glDepthMask(true);
			LWJGLRenderer.instance().update();
		}
	}

	public void render(Renderer renderer, boolean lwjgl) {
		renderer.drawPointLight(100, 100, new Color(1f, 0f, 0f, 1f), new Vector3f(0.001f, 0.005f, 0.00001f));
		renderer.drawPointLight(200, 100, new Color(0f, 1f, 0f, 1f), new Vector3f(0.001f, 0.005f, 0.00001f));
		renderer.drawPointLight(400, 100, new Color(1f, 0f, 1f, 1f), new Vector3f(0.001f, 0.005f, 0.00001f));
		renderer.drawPointLight(300, 150, new Color(0f, 1f, 1f, 1f), new Vector3f(0.001f, 0.005f, 0.00001f));

		renderer.drawPointLight(500, 500, new Color(1f, 1f, 1f, 1f), new Vector3f(0.005f, 0.005f, 0f));

		renderer.render(new Renderable() {
			@Override
			public void render(Renderer r) {
				r.drawImage(TANK_BODY, 0, 0, 1024, 1024);
			}
		});
		/*m_light.use();
		//Setup light
		Uniform lightLocation = new Uniform(m_light, "lightpos");
		lightLocation.setValue(new Vector2f(100f, 100f));
		
		Uniform lightColor = new Uniform(m_light, "lightColor");
		lightColor.setValue(new Vector3f(1f, 1f, 1f));
		
		Uniform screenHeight = new Uniform(m_light, "screenHeight");
		screenHeight.setValue(1024);
		
		Uniform lightAttenuation = new Uniform(m_light, "lightAttenuation");
		lightAttenuation.setValue(new Vector3f(0.001f, 0.0005f, 0.00001f));
		
		Uniform diffuseTexture= new Uniform(m_light, "diffuseTexture");
		diffuseTexture.setValue(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		
		renderer.drawImage(TANK_BODY, 0, 0, 1024, 1024);
		
		//m_light.clear();*/
	}

	public static void main(String[] args) {
		Thread first = new Thread(new Runnable() {
			@Override
			public void run() {
				new LWJGLTest().run(true);
			}
		});
		first.start();
		/*Thread second = new Thread(new Runnable() {
			public void run() {
				new LWJGLTest().run(false);
			}
		});
		second.start();*/
	}
}
