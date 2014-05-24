#version 110

uniform vec2 lightpos;
uniform vec3 lightColor;
uniform float screenHeight;
uniform vec3 lightAttenuation;

void main() {
	gl_Position = ftransform();
	gl_TexCoord[0] = gl_MultiTexCoord0;
}