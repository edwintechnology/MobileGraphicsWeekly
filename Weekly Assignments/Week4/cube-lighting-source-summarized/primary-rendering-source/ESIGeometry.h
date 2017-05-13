//
//  ESICube.h
//  GLES Introduction Example
//
//  Created by Blake Harrison on 2/16/14.
//  Copyright (c) 2014 Blake Harrison. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <GLKit/GLKit.h>
#import "glm.hpp"
#import "glm/ext.hpp"
#import "tiny_obj_loader.h"
#import "ESISceneConstants.h"

using namespace glm;

@interface ESIGeometry : NSObject
{
    GLint _uniformsLocal[NUMBER_OF_UNIFORMS];
    GLuint _vertexArrayObjectsLocal[NUMBER_OF_VERTEX_ARRAY_OBJECTS];
    GLuint _vertexBufferObjectsLocal[NUMBER_OF_ATTRIBUTES];
    
    tinyobj::shape_t _shape;
}

@property (nonatomic)GLint *uniforms;
@property (nonatomic)GLuint *vertexArrayObjects;
@property (nonatomic)GLuint *vertexBufferObjects;
@property (nonatomic)GLuint program;

@property (nonatomic)mat4 modelMatrix;
@property (nonatomic)mat4 viewMatrix;
@property (nonatomic)mat4 projectionMatrix;

@property (nonatomic)mat4 modelViewProjectionMatrix;
@property (nonatomic)mat4 modelViewMatrix;
@property (nonatomic)mat3 normalMatrix;

@property (nonatomic)vec3 accumulatedTranslations;
@property (nonatomic)vec3 accumulatedRotations;
@property (nonatomic)vec3 accumulatedScaling;
@property (nonatomic)mat4 accumulatedTransformations;

@property (nonatomic)vec3 materialAmbient;

@property (nonatomic)vec4 center;

- (id)initWithShape:(tinyobj::shape_t *)shape;
- (BOOL)loadShaders;
- (BOOL)compileShader:(GLuint *)shader type:(GLenum)type file:(NSString *)file;
- (BOOL)linkProgram:(GLuint)prog;
- (BOOL)validateProgram:(GLuint)prog;
- (tinyobj::shape_t)shape;
@end
