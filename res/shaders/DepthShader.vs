#version 330

layout(location = 0) in vec3 position;

uniform mat4 transform;
uniform mat4 depthMVP;

void main(){
	gl_Position = depthMVP * transform * vec4(position, 1.0);
}