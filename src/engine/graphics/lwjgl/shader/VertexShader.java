package engine.graphics.lwjgl.shader;

import java.io.File;

public class VertexShader extends Shader {
	public static final ShaderType TYPE = ShaderType.VERTEX_SHADER;

	public VertexShader() {
		super(TYPE);
	}
	public VertexShader(VertexShader src) {
		super(src);
	}
	public VertexShader(String src) {
		super(src, TYPE);
	}
	public VertexShader(File src) {
		super(src, TYPE);
	}
}