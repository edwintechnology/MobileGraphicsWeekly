package com.example.opengles20_projection;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.util.Log;

public class Sphere {
	private final String vertexShaderCode =
	    	"uniform mat4 uModelMatrix;" +
	    	"uniform mat4 uViewMatrix;" +
	    	"uniform mat4 uProjectionMatrix;" +
	        "attribute vec4 vPosition;" +
	        "void main() {" +
	        " mat4 MV = uViewMatrix * uModelMatrix;" +
	        " mat4 MVP = uProjectionMatrix * MV;" +
	        "  gl_Position = MVP * vPosition;" +
	        "}";

	    private final String fragmentShaderCode =
	        "precision mediump float;" +
	        "uniform vec4 vColor;" +
	        "void main() {" +
	        "  gl_FragColor = vColor;" +
	        "}";

	static private FloatBuffer sphereVertex;
    static private FloatBuffer sphereNormal;
    static float sphere_parms[]=new float[3];
    
    private final int mProgram;
    
    private int mPositionHandle;
    private int mColorHandle;
    private int mModelMatrixHandle;
    private int mViewMatrixHandle;
    private int mProjectionMatrixHandle; 
    
    private final int COORDS_PER_VERTEX = 4;
    private final int vertexCount;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // bytes per vertex
    double mRaduis;
    double mStep;
    float mVertices[];
    float color[] = {1.0f, 1.0f, 1.0f, 1.0f};
    private static double DEG = Math.PI/180;
    int mPoints;

    /**
     * The value of step will define the size of each facet as well as the number of facets
     *  
     * @param radius
     * @param step
     */
    private double DEGREES_TO_RADIANS(int deg)
    {
    	return deg * Math.PI / 180;
    }
    public Sphere( float radius, double step) {
        this.mRaduis = radius;
        this.mStep = step;
        
        float vertices[] = new float[720];
        for (int i = 0; i < 720; i += 2) {
        // x value
        vertices[i] = (float) (Math.cos(DEGREES_TO_RADIANS(i/2)) * 0.4); // x=r *cos(theta)
        // y value
        vertices[i+1] = (float) (Math.sin(DEGREES_TO_RADIANS(i/2)) * 0.6);// y= r*sin(theta)
        }
        Vertices[0]= r* sin(thita)*cos(fy);
        Vertices[1]= r*sin(thita)*sin(fy);
        Vertices[2]= r*cos(thita);
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		vertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        sphereVertex = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        sphereVertex.put(vertices);
        // set the buffer to read the first coordinate
        sphereVertex.position(0);
        
        vertexCount = vertices.length / COORDS_PER_VERTEX;
        
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        
        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program
    }

    public void draw() {
    	// Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);
        
        // rotate the cube
        //rotate(); 
        
        // get the location of the matrices uniform
        mModelMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uModelMatrix");
        mViewMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uViewMatrix");
        mProjectionMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uProjectionMatrix");
        
        // send the projection matrix to the shader
        GLES20.glUniformMatrix4fv(mModelMatrixHandle, 1, false, MyGLRenderer.mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(mViewMatrixHandle, 1, false, MyGLRenderer.mViewMatrix, 0);
        GLES20.glUniformMatrix4fv(mProjectionMatrixHandle, 1, false, MyGLRenderer.mProjectionMatrix, 0);
            
        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, sphereVertex);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        
        // Draw each of the faces of the cube with a different color
            // Set color for drawing the side of the cube
        	GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        	
	        // Draw the face
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    void SolidSphere(float radius, int rings, int sectors)
    {
        float R = 1 /(float)(rings-1);
        float S = 1 /(float)(sectors-1);
        int r, s;

        for(r = 0; r < rings; r++) for(s = 0; s < sectors; s++) {
                float y = sin( -M_PI_2 + M_PI * r * R );
                float x = cos(2*M_PI * s * S) * sin( M_PI * r * R );
                float z = sin(2*M_PI * s * S) * sin( M_PI * r * R );

                *t++ = s*S;
                *t++ = r*R;

                *v++ = x * radius;
                *v++ = y * radius;
                *v++ = z * radius;

                *n++ = x;
                *n++ = y;
                *n++ = z;
        }

        sphere_indices.resize(rings * sectors * 4);
        std:vector<GLushort>::iterator i = sphere_indices.begin();
        for(r = 0; r < rings; r++) for(s = 0; s < sectors; s++) {
                *i++ = r * sectors + s;
                *i++ = r * sectors + (s+1);
                *i++ = (r+1) * sectors + (s+1);
                *i++ = (r+1) * sectors + s;
        }
    }
}
