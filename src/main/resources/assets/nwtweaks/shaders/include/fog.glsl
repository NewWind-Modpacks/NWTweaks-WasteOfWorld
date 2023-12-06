#version 150

float calcFogIntensity(float value, float fogStart, float fogEnd) {
    float maxFog = (fogEnd - fogStart);
    value = (value - fogStart) / maxFog;

    float x = value;

    //x = x; // Linear
    //x = 1 - (1 - x) * (1 - x); // EaseOutQuad
    //x = x == 1 ? 1 : 1 - pow(2, -10 * x); // EaseOutExpo
    //x = 1 - pow(1 - x, 3); // EaseOutCubic
    x = sqrt(1 - pow(x - 1, 2)); // EaseOutCirc

    return (x * maxFog) + fogStart;
}

vec4 linear_fog(vec4 inColor, float vertexDistance, float fogStart, float fogEnd, vec4 fogColor) {
    if (vertexDistance <= fogStart) {
        return inColor;
    }

    float fogValue = vertexDistance < fogEnd ? smoothstep(fogStart, fogEnd, calcFogIntensity(vertexDistance, fogStart, fogEnd)) : 1.0;
    return vec4(mix(inColor.rgb, fogColor.rgb, fogValue * fogColor.a), inColor.a);
}

float linear_fog_fade(float vertexDistance, float fogStart, float fogEnd) {
    if (vertexDistance <= fogStart) {
        return 1.0;
    } else if (vertexDistance >= fogEnd) {
        return 0.0;
    }

    return smoothstep(fogEnd, fogStart, calcFogIntensity(vertexDistance, fogStart, fogEnd));
}

float fog_distance(mat4 modelViewMat, vec3 pos, int shape) {
    if (shape == 0) {
        return length((modelViewMat * vec4(pos, 1.0)).xyz);
    } else {
        float distXZ = length((modelViewMat * vec4(pos.x, 0.0, pos.z, 1.0)).xyz);
        float distY = length((modelViewMat * vec4(0.0, pos.y, 0.0, 1.0)).xyz);
        return max(distXZ, distY);
    }
}
