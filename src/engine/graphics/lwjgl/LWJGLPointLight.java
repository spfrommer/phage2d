package engine.graphics.lwjgl;

import org.lwjgl.opengl.GL13;

import engine.graphics.Color;
import engine.graphics.lwjgl.shader.FragmentShader;
import engine.graphics.lwjgl.shader.Program;
import engine.graphics.lwjgl.shader.Shader.ShaderCompileException;
import engine.graphics.lwjgl.shader.Uniform;
import engine.graphics.lwjgl.shader.VertexShader;
import engine.graphics.lwjgl.vector.Vector2f;
import engine.graphics.lwjgl.vector.Vector3f;

public class LWJGLPointLight {
	private static Program s_pointProgram;
	private static Uniform s_lightLocation;
	private static Uniform s_lightColor;
	private static Uniform s_lightAttenuation;
	private static Uniform s_diffuseTexture;
	private static int s_displayHeight;

	public static void init(int width, int height) {
		s_displayHeight = height;
		s_pointProgram = new Program();
		FragmentShader fragment = new FragmentShader();
		VertexShader vertex = new VertexShader();
		try {
			fragment.setSource(LWJGLTest.class.getResourceAsStream("/shaders/lighting.frag"));
			fragment.compile();
			vertex.setSource(LWJGLTest.class.getResourceAsStream("/shaders/lighting.vert"));
			vertex.compile();
		} catch (ShaderCompileException e) {
			e.printStackTrace();
		}
		s_pointProgram.attachShader(fragment);
		s_pointProgram.attachShader(vertex);
		s_pointProgram.link();
		s_pointProgram.validate();
		// setup uniforms
		s_lightLocation = new Uniform(s_pointProgram, "lightpos");

		s_lightColor = new Uniform(s_pointProgram, "lightColor");

		s_lightAttenuation = new Uniform(s_pointProgram, "lightAttenuation");

		s_diffuseTexture = new Uniform(s_pointProgram, "diffuseTexture");
	}

	private Vector3f m_attenuation;
	private Color m_color;
	// Display relative position
	private Vector2f m_position;

	public LWJGLPointLight(Vector2f pos, Color color, Vector3f attenuation) {
		m_position = pos;
		m_color = color;
		m_attenuation = attenuation;
	}

	public void setAttenuation(Vector3f attenuation) {
		m_attenuation = attenuation;
	}

	public Vector3f getAttenuation() {
		return m_attenuation;
	}

	public void setColor(Color color) {
		m_color = color;
	}

	public Color getColor() {
		return m_color;
	}

	public Vector2f getPosition() {
		return m_position;
	}

	public void setPosition(Vector2f position) {
		m_position = position;
	}

	public void apply() {
		s_pointProgram.use();

		s_lightLocation.setValue(new Vector2f(getPosition().getX(), s_displayHeight - getPosition().getY()));

		s_lightColor.setValue(new Vector3f(getColor().getRed(), getColor().getGreen(), getColor().getBlue()));
		s_lightAttenuation.setValue(getAttenuation());

		s_diffuseTexture.setValue(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	}

	public void clear() {
		s_pointProgram.clear();
	}
}
