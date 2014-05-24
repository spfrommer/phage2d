#version 110

uniform vec4 color;
uniform sampler2D diffuseTexture;

void main() {
	gl_FragColor = color * texture2D(diffuseTexture, gl_TexCoord[0].st);
}