#version 330

in vec2 texCoord0;

uniform vec3 baseColor;
uniform sampler2D sampler;

out vec4 fragColor;

void main(){
	vec4 textureColor = texture2D(sampler, texCoord0.xy);
	
	vec4 color = vec4(baseColor, 1);
	
	if(textureColor != vec4(0,0,0,1))
		color = textureColor;
		
	fragColor = color;
}