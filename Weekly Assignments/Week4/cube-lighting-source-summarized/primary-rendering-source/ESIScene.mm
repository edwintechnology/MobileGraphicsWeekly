//
//  ESIScene.m
//  GLES Introduction Example
//
//  Created by Blake Harrison on 2/21/14.
//  Copyright (c) 2014 Blake Harrison. All rights reserved.
//

#import "ESIScene.h"

@implementation ESIScene

- (id)initWithOBJandMTLFilePath:(NSString *)fileName
{
    self = [self init];
    
    if(self){
        self.geometries = [[NSMutableArray alloc] init];
        string err = LoadObj(_shapes, (char *)[[[NSBundle mainBundle] pathForResource:fileName ofType:@"obj"] UTF8String],
                             (char *)[[[NSBundle mainBundle] pathForResource:fileName ofType:@"mtl"] UTF8String]);
        
        if (!err.empty()) {
            NSLog(@"Geometry could not successfully be loaded \n %s",err.c_str());
        }
        
        self.numberOfLights = 1;
        self.numberOfActiveLights = 1;
        
        self.pointLightSceneAmbientIntensity = vec4(1.0f, 1.0f, 1.0f, 1.0f);
        
        _pointLightsLocal.push_back(vec4(-0.35942f,0.53081f, 0.99059f,1.0f));
        
        _pointLightDiffuseIntensitiesLocal.push_back(vec4(2.5f, 2.5f, 2.5f, 1.0f));
        _pointLightSpecularIntensitiesLocal.push_back(vec4(.8f, .8f, .8f,1.0f));
        
        _nullLightsLocal.push_back(vec4(0.0f, 0.0f, 0.0f, 0.0f));
        
        _cameraPositionsLocal.push_back(vec4(vec3(0.0f,0.0f, 1.0f),1.0f));
        _cameraLookatVectorsLocal.push_back(vec3(0.0f,0.0f,0.0f));
        _cameraUpVectorsLocal.push_back(vec3(0.0f, 1.0f, 0.000001f));
        self.currentPointLightIndex = 0;
        self.currentNullLightIndex = 0;
        
        self.pointLights = &_pointLightsLocal;
        self.pointLightDiffuseIntensities = &_pointLightDiffuseIntensitiesLocal;
        self.pointLightSpecularIntensities = &_pointLightSpecularIntensitiesLocal;
        self.nullLights = &_nullLightsLocal;
        
        self.cameraPositions = &_cameraPositionsLocal;
        self.cameraLookatVectors = &_cameraLookatVectorsLocal;
        self.cameraUpVectors = &_cameraUpVectorsLocal;
        
        GLfloat maxCoordinate = 0.0f;
        GLfloat minCoordinate = 0.0f;
        // Normalize vertices and normals
        size_t i = 0;
        for (; i < _shapes.size(); i++) {
            size_t c = 0;
            for(; c < _shapes[i].mesh.positions.size(); c++){
                if(_shapes[i].mesh.positions[c] > maxCoordinate){
                    maxCoordinate = _shapes[i].mesh.positions[c];
                }
                if(_shapes[i].mesh.positions[c] < minCoordinate){
                    minCoordinate = _shapes[i].mesh.positions[c];
                }
            }
        }
        
        if(maxCoordinate > 1 || minCoordinate < -1){
            GLfloat coordinateWithGreatestMagnitude = 0.0f;
            GLfloat maxCoordinateABS = abs(maxCoordinate);
            GLfloat minCoordinateABS = abs(minCoordinate);
            
            if(maxCoordinateABS > minCoordinateABS){
                coordinateWithGreatestMagnitude = maxCoordinateABS;
            }
            else{
                coordinateWithGreatestMagnitude = minCoordinateABS;
            }
            
            size_t k = 0;
            for (; k < _shapes.size(); k++) {
                size_t c = 0;
                for(; c < _shapes[i].mesh.positions.size(); c++){
                    _shapes[i].mesh.positions[c] = _shapes[i].mesh.positions[c] / coordinateWithGreatestMagnitude;
                }
            }
            
            NSInteger numberOfCameraPositions = _cameraPositionsLocal.size();
            NSInteger c = 0;
            for(; c < numberOfCameraPositions; c++){
                _cameraPositionsLocal[c].x = _cameraPositionsLocal[c].x / coordinateWithGreatestMagnitude;
                _cameraPositionsLocal[c].y = _cameraPositionsLocal[c].y / coordinateWithGreatestMagnitude;
                _cameraPositionsLocal[c].z = _cameraPositionsLocal[c].z / coordinateWithGreatestMagnitude;
                
                _cameraLookatVectorsLocal[c].x = _cameraLookatVectorsLocal[c].x / coordinateWithGreatestMagnitude;
                _cameraLookatVectorsLocal[c].y = _cameraLookatVectorsLocal[c].y / coordinateWithGreatestMagnitude;
                _cameraLookatVectorsLocal[c].z = _cameraLookatVectorsLocal[c].z / coordinateWithGreatestMagnitude;
                
                _cameraUpVectorsLocal[c].x = _cameraUpVectorsLocal[c].x / coordinateWithGreatestMagnitude;
                _cameraUpVectorsLocal[c].y = _cameraUpVectorsLocal[c].y / coordinateWithGreatestMagnitude;
                _cameraUpVectorsLocal[c].z = _cameraUpVectorsLocal[c].z / coordinateWithGreatestMagnitude;
            }
            
            NSInteger numberOfPointLights = _pointLightsLocal.size();
            NSInteger z = 0;
            for(; z < numberOfPointLights; z++){
                _pointLightsLocal[c].x = _pointLightsLocal[c].x / coordinateWithGreatestMagnitude;
                _pointLightsLocal[c].y = _pointLightsLocal[c].y / coordinateWithGreatestMagnitude;
                _pointLightsLocal[c].z = _pointLightsLocal[c].z / coordinateWithGreatestMagnitude;
            }
        }
        
        NSInteger count = _shapes.size();
        NSInteger c = 0;
        for(; c < count; c++){
            [self.geometries addObject:[[ESIGeometry alloc]initWithShape:&_shapes[c]]];
        }
    }
    
    return self;
}

- (std::vector<float>)normalizeTriangleVertices:(std::vector<float>)vectorList{
    long long count = vectorList.size();
    vec3 tempVector;
    std::vector<float> newVectorList;
    long long c = 0;
    for(; c < count; c += 3){
        tempVector = normalize(vec3(vectorList[c], vectorList[c+1], vectorList[c+2]));
        newVectorList.push_back(tempVector.x);
        newVectorList.push_back(tempVector.y);
        newVectorList.push_back(tempVector.z);
    }
    
    return newVectorList;
}
@end
