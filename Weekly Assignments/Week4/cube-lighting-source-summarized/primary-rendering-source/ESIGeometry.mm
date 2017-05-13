//
//  ESIGeometry.m
//  GLES Introduction Example
//
//  Created by Blake Harrison on 2/16/14.
//  Copyright (c) 2014 Blake Harrison. All rights reserved.
//

#import "ESIGeometry.h"
using namespace glm;
using namespace std;
using namespace tinyobj;

@interface ESIGeometry ()
@end

@implementation ESIGeometry

#pragma mark -  OpenGL ES 2 shader compilation

- (id)initWithShape:(tinyobj::shape_t *)shape{
    
    self = [super init];
    
    if(self){
        self.uniforms = &(_uniformsLocal[0]);
        self.vertexArrayObjects = &(_vertexArrayObjectsLocal[0]);
        self.vertexBufferObjects = &(_vertexBufferObjectsLocal[0]);
        _shape = *shape;
        
        self.center = vec4(0.0f, 0.0f, 0.0f, 0.0f);
        
        GLfloat maxX = 0.0f, maxY = 0.0f, maxZ = 0.0f;
        GLfloat minX = 0.0f, minY = 0.0f, minZ = 0.0f;
        
        NSInteger count = _shape.mesh.positions.size();
        NSInteger c = 0;
        for(; c < count; c+=3){
            if(_shape.mesh.positions[c] > maxX)
            {
                maxX = _shape.mesh.positions[c];
            }
            
            if(_shape.mesh.positions[c] < minX)
            {
                minX = _shape.mesh.positions[c];
            }
            
            if(_shape.mesh.positions[c+1] > maxY)
            {
                maxY = _shape.mesh.positions[c+1];
            }
            
            if(_shape.mesh.positions[c+1] < minY)
            {
                minY = _shape.mesh.positions[c+1];
            }
            
            if(_shape.mesh.positions[c+2] > maxZ)
            {
                maxZ = _shape.mesh.positions[c+2];
            }
            
            if(_shape.mesh.positions[c+2] < minZ)
            {
                minZ = _shape.mesh.positions[c+2];
            }
        }
        self.center = vec4((maxX + minX) /2, (maxY + minY) /2, (maxZ + minZ) /2, 1.0f);
        
        self.materialAmbient = vec3(1.0f, 1.0f, 1.0f);
    }
    
    return self;
}

-(tinyobj::shape_t)shape{
    return _shape;
}

- (BOOL)loadShaders
{
    GLuint vertShader, fragShader;
    NSString *vertShaderPathname, *fragShaderPathname;
    
    // Create shader program.
    self.program = glCreateProgram();
    
    // Create and compile vertex shader.
    vertShaderPathname = [[NSBundle mainBundle] pathForResource:@"VertexShader" ofType:@"vsh"];
    if (![self compileShader:&vertShader type:GL_VERTEX_SHADER file:vertShaderPathname]) {
        NSLog(@"Failed to compile vertex shader");
        return NO;
    }
    
    // Create and compile fragment shader.
    fragShaderPathname = [[NSBundle mainBundle] pathForResource:@"FragmentShader" ofType:@"fsh"];
    if (![self compileShader:&fragShader type:GL_FRAGMENT_SHADER file:fragShaderPathname]) {
        NSLog(@"Failed to compile fragment shader");
        return NO;
    }
    
    // Attach vertex shader to program.
    glAttachShader(self.program, vertShader);
    
    // Attach fragment shader to program.
    glAttachShader(self.program, fragShader);
    
    // Bind attribute locations.
    // This needs to be done prior to linking.
    glBindAttribLocation(self.program, VERTEX_POS_ATTRIB, "a_position");
    glBindAttribLocation(self.program, VERTEX_NORMAL_ATTRIB, "a_normal");
    
    // Link program.
    if (![self linkProgram:self.program]) {
        NSLog(@"Failed to link program: %d", self.program);
        
        if (vertShader) {
            glDeleteShader(vertShader);
            vertShader = 0;
        }
        if (fragShader) {
            glDeleteShader(fragShader);
            fragShader = 0;
        }
        if (self.program) {
            glDeleteProgram(self.program);
            self.program = 0;
        }
        
        return NO;
    }
    
    // Get uniform locations.
    _uniforms[MODELVIEWPROJECTION_MATRIX_UNIFORM] = glGetUniformLocation(self.program, "modelViewProjectionMatrix");
    _uniforms[NORMAL_MATRIX_UNIFORM] = glGetUniformLocation(self.program, "normalMatrix");
    _uniforms[MODELVIEW_MATRIX_UNIFORM] = glGetUniformLocation(self.program, "modelViewMatrix");
    
    _uniforms[MATERIAL_AMBIENT_UNIFORM] = glGetUniformLocation(self.program, "materialAmbient");
    _uniforms[MATERIAL_DIFFUSE_UNIFORM] = glGetUniformLocation(self.program, "materialDiffuse");
    _uniforms[MATERIAL_SPECULAR_UNIFORM] = glGetUniformLocation(self.program, "materialSpecular");
    _uniforms[MATERIAL_SPECULAR_EXP_UNIFORM] = glGetUniformLocation(self.program, "materialSpecularExponent");
    
    _uniforms[LIGHT_SCENE_AMBIENT_INTENSITY_UNIFORM] = glGetUniformLocation(self.program, "sceneAmbientIntensity");
    
    _uniforms[LIGHT0_POSITION_UNIFORM] = glGetUniformLocation(self.program, "light0Position");
    _uniforms[LIGHT0_DIFFUSE_INTENSITY_UNIFORM] = glGetUniformLocation(self.program, "light0DiffuseIntensity");
    _uniforms[LIGHT0_SPECULAR_INTENSITY_UNIFORM] = glGetUniformLocation(self.program, "light0SpecularIntensity");
    
    // Release vertex and fragment shaders.
    if (vertShader) {
        glDetachShader(self.program, vertShader);
        glDeleteShader(vertShader);
        vertShader = 0;
    }
    if (fragShader) {
        glDetachShader(self.program, fragShader);
        glDeleteShader(fragShader);
        fragShader = 0;
    }
    
    return YES;
}

- (BOOL)compileShader:(GLuint *)shader type:(GLenum)type file:(NSString *)file
{
    GLint status;
    const GLchar *source;
    
    source = (GLchar *)[[NSString stringWithContentsOfFile:file encoding:NSUTF8StringEncoding error:nil] UTF8String];
    if (!source) {
        NSLog(@"Failed to load vertex shader");
        return NO;
    }
    
    *shader = glCreateShader(type);
    glShaderSource(*shader, 1, &source, NULL);
    glCompileShader(*shader);
    
#if defined(DEBUG)
    GLint logLength;
    glGetShaderiv(*shader, GL_INFO_LOG_LENGTH, &logLength);
    if (logLength > 0) {
        GLchar *log = (GLchar *)malloc(logLength);
        glGetShaderInfoLog(*shader, logLength, &logLength, log);
        NSLog(@"Shader compile log:\n%s", log);
        free(log);
    }
#endif
    
    glGetShaderiv(*shader, GL_COMPILE_STATUS, &status);
    if (status == 0) {
        glDeleteShader(*shader);
        return NO;
    }
    
    return YES;
}

- (BOOL)linkProgram:(GLuint)prog
{
    GLint status;
    glLinkProgram(prog);
    
#if defined(DEBUG)
    GLint logLength;
    glGetProgramiv(prog, GL_INFO_LOG_LENGTH, &logLength);
    if (logLength > 0) {
        GLchar *log = (GLchar *)malloc(logLength);
        glGetProgramInfoLog(prog, logLength, &logLength, log);
        NSLog(@"Program link log:\n%s", log);
        free(log);
    }
#endif
    
    glGetProgramiv(prog, GL_LINK_STATUS, &status);
    if (status == 0) {
        return NO;
    }
    
    return YES;
}

- (BOOL)validateProgram:(GLuint)prog
{
    GLint logLength, status;
    glValidateProgram(prog);
    glGetProgramiv(prog, GL_INFO_LOG_LENGTH, &logLength);
    if (logLength > 0) {
        GLchar *log = (GLchar *)malloc(logLength);
        glGetProgramInfoLog(prog, logLength, &logLength, log);
        NSLog(@"Program validate log:\n%s", log);
        free(log);
    }
    
    glGetProgramiv(prog, GL_VALIDATE_STATUS, &status);
    if (status == 0) {
        return NO;
    }
    
    return YES;
}

@end
