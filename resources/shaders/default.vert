#version 110

uniform vec4 color;
uniform sampler2D diffuseTexture;

void main() {
	gl_Position = ftransform();
	gl_TexCoord[0] = gl_MultiTexCoord0;
}