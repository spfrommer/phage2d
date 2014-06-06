#version 110

uniform vec2 lightpos;
uniform vec3 lightColor;
uniform vec3 lightAttenuation;

uniform vec4 color;

uniform sampler2D diffuseTexture;

void main()
{		
	vec2 pixel=gl_FragCoord.xy;
	vec2 aux=lightpos-pixel;
	float distance=length(aux);
	float attenuation=1.0/(lightAttenuation.x+lightAttenuation.y*distance+lightAttenuation.z*distance*distance);	
	vec4 fragLightColor=vec4(attenuation,attenuation,attenuation, pow(attenuation, 3)) * vec4(lightColor,1);	
	gl_FragColor = color * fragLightColor * texture2D(diffuseTexture, gl_TexCoord[0].st);
}