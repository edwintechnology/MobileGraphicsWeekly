package com.example.opengles20_orientation;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Cube {


    private final String vertexShaderCode =
    	"uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "void main() {" +
        "  gl_Position = uMVPMatrix * vPosition;" +
        "}";

    private final String fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 vColor;" +
        "void main() {" +
        "  gl_FragColor = vColor;" +
        "}";

    private final FloatBuffer vertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    
    private final int CUBE_FACES = 6; 	// number of faces on a cube;
    private float current_rotation = 0f;
    private final float ROTATION_CHANGE = 1f;
    
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 4;
	static float cubeCoords[] = { // in counterclockwise order: (x, y, z, w)
			// front face
			0.5f, 0.5f, 0.5f, 1.0f, 		// top right
			-0.5f, -0.5f, 0.5f, 1.0f, 		// bottom left
			0.5f, -0.5f, 0.5f, 1.0f,		// bottom right

			0.5f, 0.5f, 0.5f, 1.0f, 		// top right
			-0.5f, 0.5f, 0.5f, 1.0f,		// top left
			-0.5f, -0.5f, 0.5f, 1.0f, 		// bottom left

			// left face
			-0.5f, 0.5f, 0.5f, 1.0f, 		// top right
			-0.5f, -0.5f, -0.5f, 1.0f, 		// bottom left
			-0.5f, -0.5f, 0.5f, 1.0f, 		// bottom right

			-0.5f, 0.5f, 0.5f, 1.0f, 		// top right
			-0.5f, 0.5f, -0.5f, 1.0f, 		// top left
			-0.5f, -0.5f, -0.5f, 1.0f,		// bottom left

			// back face
			-0.5f, 0.5f, -0.5f, 1.0f, 		// top right
			0.5f, -0.5f, -0.5f, 1.0f, 		// bottom left
			-0.5f, -0.5f, -0.5f, 1.0f, 		// bottom right

			-0.5f, 0.5f, -0.5f, 1.0f, 		// top right
			0.5f, 0.5f, -0.5f, 1.0f, 		// top left
			0.5f, -0.5f, -0.5f, 1.0f, 		// bottom left

			// right face
			0.5f, 0.5f, -0.5f, 1.0f, 		// top right
			0.5f, -0.5f, 0.5f, 1.0f, 		// bottom left
			0.5f, -0.5f, -0.5f, 1.0f, 		// bottom right

			0.5f, 0.5f, -0.5f, 1.0f, 		// top right
			0.5f, 0.5f, 0.5f, 1.0f, 		// top left
			0.5f, -0.5f, 0.5f, 1.0f, 		// bottom left

			// top face
			0.5f, 0.5f, -0.5f, 1.0f, 		// top right
			-0.5f, 0.5f, 0.5f, 1.0f, 		// bottom left
			0.5f, 0.5f, 0.5f, 1.0f, 		// bottom right

			0.5f, 0.5f, -0.5f, 1.0f, 		// top right
			-0.5f, 0.5f, -0.5f, 1.0f, 		// top left
			-0.5f, 0.5f, 0.5f, 1.0f, 		// bottom left

			// bottom face
			0.5f, -0.5f, 0.5f, 1.0f, 		// top right
			-0.5f, -0.5f, -0.5f, 1.0f, 		// bottom left
			0.5f, -0.5f, -0.5f, 1.0f, 		// bottom right

			0.5f, -0.5f, 0.5f, 1.0f, 		// top right
			-0.5f, -0.5f, 0.5f, 1.0f, 		// top left
			-0.5f, -0.5f, -0.5f, 1.0f 		// bottom left
	};
	
    private final int vertexCount = cubeCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // bytes per vertex
        
    // Set color with red, green, blue and alpha (opacity) values
    float color[][] = { 
    		{1.0f, 0.0f, 0.0f, 1.0f}, 	// front face
    		{0.0f, 1.0f, 0.0f, 1.0f}, 	// left face
    		{0.0f, 0.0f, 1.0f, 1.0f}, 	// back face
    		{1.0f, 1.0f, 0.0f, 1.0f}, 	// right face
    		{1.0f, 0.0f, 1.0f, 1.0f}, 	// top face
    		{0.0f, 1.0f, 1.0f, 1.0f}	// bottom face
    };
          

    public Cube() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		cubeCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(cubeCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
        
        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        
        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program
    }

    
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);
        
        // rotate the cube
        //rotate(); 
        
        // get the location of the matrix uniform
        //MyGLRenderer.uMatrixLocation = GLES20.glGetUniformLocation(mProgram, "u_Matrix");
        
        // send the projection matrix to the shader
        //GLES20.glUniformMatrix4fv(MyGLRenderer.uMatrixLocation, 1, false, MyGLRenderer.modelMatrix, 0);
            
        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        
        // Draw each of the faces of the cube with a different color
        for(int i = 0; i < CUBE_FACES; i++) {
	        // Set color for drawing the side of the cube
        	GLES20.glUniform4fv(mColorHandle, 1, color[i], 0);
        	
        	GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
	        // Draw the face
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, i * CUBE_FACES, vertexCount / CUBE_FACES);
        }
        
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
    
    
    /*public void rotate() {
        // translation
        Matrix.setIdentityM(MyGLRenderer.modelMatrix, 0);
        Matrix.translateM(MyGLRenderer.modelMatrix, 0, 0f, 0f, -4f);
        
        // update the amount to rotate
        current_rotation = current_rotation >= 360 ? 0 : current_rotation + ROTATION_CHANGE;
        
        // rotation (x and y axis)
        Matrix.rotateM(MyGLRenderer.modelMatrix, 0, current_rotation, 1f, 1f, 0f);
        
        // multiply the model and projection matrices
        final float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, MyGLRenderer.projectionMatrix, 0, MyGLRenderer.modelMatrix, 0);
        System.arraycopy(temp, 0, MyGLRenderer.modelMatrix, 0, temp.length);
    }*/
}
