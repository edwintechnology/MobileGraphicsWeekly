//
//  Shader.vsh
//  GLKit Example
//
//  Created by Blake Harrison on 1/29/14.
//  Copyright (c) 2014 Blake Harrison. All rights reserved.
//

attribute vec4 a_position;
attribute vec3 a_normal;

uniform mat4 modelViewProjectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat3 normalMatrix;

varying vec3 v_cameraspace_normal;
varying vec4 v_cameraspace_position;

void main()
{
    v_cameraspace_normal = normalMatrix * a_normal;
    v_cameraspace_position = modelViewMatrix * a_position;
    gl_Position = modelViewProjectionMatrix * a_position;
}