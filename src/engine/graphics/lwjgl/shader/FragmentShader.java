package engine.graphics.lwjgl.shader;

import java.io.File;

public class FragmentShader extends Shader {
	public static final ShaderType TYPE = ShaderType.FRAGMENT_SHADER;

	public FragmentShader() {
		super(TYPE);
	}
	public FragmentShader(FragmentShader src) {
		super(src);
	}
	public FragmentShader(String src) {
		super(src, TYPE);
	}
	public FragmentShader(File src) {
		super(src, TYPE);
	}
}