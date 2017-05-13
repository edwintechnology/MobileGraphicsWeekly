//
//  Shader.fsh
//  GLKit Example
//
//  Created by Blake Harrison on 1/29/14.
//  Copyright (c) 2014 Blake Harrison. All rights reserved.
//

precision highp float;
precision highp int;

uniform vec3 materialAmbient;
uniform vec3 materialDiffuse;
uniform vec3 materialSpecular;
uniform float materialSpecularExponent;

uniform vec4 sceneAmbientIntensity;

uniform vec4 light0Position;
uniform vec4 light0DiffuseIntensity;
uniform vec4 light0SpecularIntensity;

varying vec3 v_cameraspace_normal;
varying vec4 v_cameraspace_position;

struct lightSource
{
    vec4 position;
    vec4 diffuse;
    vec4 specular;
    float constantAttenuation, linearAttenuation, quadraticAttenuation;
};

vec3 ambientTerms(){
    return sceneAmbientIntensity.xyz * materialAmbient;
}

float diffuseTerm(in vec3 normal, in vec3 lightDirection){
    float diffuseTerm = max(0.0, dot(normal, lightDirection));
    return  diffuseTerm;
}

float specularTerm( in vec3 lightDirection, in vec3 normal, in vec3 viewDirection){
    highp vec3 reflectDirection = normalize(reflect(-lightDirection, normal));
    highp float specularTerm = max(0.0, dot(reflectDirection, viewDirection));
    if(materialSpecularExponent != 0.0){
        specularTerm = pow(specularTerm, materialSpecularExponent);
    }
    else{
        specularTerm = 0.0;
    }
    
    return specularTerm;
}

vec3 phongReflection(in lightSource light, in vec3 color, in vec3 normal, in vec3 viewDirection){
    highp vec3 lightDirection;
    float attenuation;
    
    if(light.position.w == 0.0){
        lightDirection = normalize(vec3(light.position));
        attenuation = 1.0;
    }
    else{
        lightDirection = vec3(light.position - v_cameraspace_position);
        float distance = length(lightDirection);
        attenuation = 1.0 / (light.constantAttenuation + light.linearAttenuation * distance + light.quadraticAttenuation * distance * distance);
        lightDirection = normalize(lightDirection);
    }
    
    return color + light.diffuse.xyz * diffuseTerm(normal, lightDirection) * materialDiffuse * attenuation + light.specular.xyz * specularTerm(lightDirection, normal, viewDirection) * materialSpecular * attenuation;
}

void main()
{
    const int numberOfLights = 1;
    
    lightSource lights[numberOfLights];
    
    lightSource light0 = lightSource(
        light0Position,
        light0DiffuseIntensity,
        light0SpecularIntensity,
        1.0, 0.0, 0.0
    );
    
    lights[0] = light0;
    
    highp vec3 normal = normalize(v_cameraspace_normal);
    highp vec3 viewDirection = vec3(0.0, 0.0, 1.0);
    
    highp vec3 color = ambientTerms();
    color = phongReflection(lights[0], color, normal, viewDirection);
    color.x = min(1.0, color.x);
    color.y = min(1.0, color.y);
    color.z = min(1.0, color.z);
    
    gl_FragColor = vec4(color, 1.0);
}