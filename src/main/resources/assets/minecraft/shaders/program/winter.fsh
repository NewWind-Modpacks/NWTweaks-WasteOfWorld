#version 150
//Shader copied from Sanity - Descent into Madness.

uniform sampler2D DiffuseSampler;
uniform float DesaturateFactor;
uniform float Gray;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);

	float Luma = dot(color.rgb, vec3(Gray, Gray, Gray));
	vec3 Chroma = color.rgb - Luma;
	color.rgb = (Chroma * (1.0 - DesaturateFactor)) + Luma;

    fragColor = color;
}