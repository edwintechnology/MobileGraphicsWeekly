//
//  ESIScene.h
//  GLES Introduction Example
//
//  Created by Blake Harrison on 2/21/14.
//  Copyright (c) 2014 Blake Harrison. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <GLKit/GLKit.h>
#import "glm.hpp"
#import "glm/ext.hpp"
#import "tiny_obj_loader.h"
#import "ESIGeometry.h"
#import "ESISceneConstants.h"

using namespace std;

@interface ESIScene : NSObject
{
    vector<tinyobj::shape_t> _shapes;
    
    vector<vec4> _pointLightsLocal;
    vector<vec4> _cameraPositionsLocal;
    vector<vec3> _cameraLookatVectorsLocal;
    vector<vec3> _cameraUpVectorsLocal;
    
    vector<vec4> _pointLightDiffuseIntensitiesLocal;
    vector<vec4> _pointLightSpecularIntensitiesLocal;
    vector<vec4> _nullLightsLocal;
}

@property (strong, nonatomic)NSMutableArray *geometries;

@property (nonatomic)vector<vec4> *cameraPositions;
@property (nonatomic)vector<vec3> *cameraLookatVectors;
@property (nonatomic)vector<vec3> *cameraUpVectors;

@property (nonatomic)vector<vec4> *pointLights;
@property (nonatomic)NSInteger currentPointLightIndex;
@property (nonatomic)NSInteger currentNullLightIndex;

@property (nonatomic)vec4 pointLightSceneAmbientIntensity;
@property (nonatomic)vector<vec4> *pointLightDiffuseIntensities;
@property (nonatomic)vector<vec4> *pointLightSpecularIntensities;
@property (nonatomic)vector<vec4> *nullLights;
@property (nonatomic)GLint numberOfLights;
@property (nonatomic)GLint numberOfActiveLights;

- (id)initWithOBJandMTLFilePath:(NSString *)fileName;

- (std::vector<float>)normalizeTriangleVertices:(std::vector<float>)vectorList;

@end
