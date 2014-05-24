package engine.graphics.lwjgl.shader;

import java.io.File;

public class GeometryShader extends Shader {
	
	public GeometryShader() {
		super(ShaderType.GEOMETRY_SHADER);
	}
	public GeometryShader(GeometryShader src) {
		super(src);
	}
	public GeometryShader(String src) {
		super(src, ShaderType.GEOMETRY_SHADER);
	}
	public GeometryShader(File src) {
		super(src, ShaderType.GEOMETRY_SHADER);
	}
}