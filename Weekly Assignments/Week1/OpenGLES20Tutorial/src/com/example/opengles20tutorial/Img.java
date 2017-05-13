package com.example.opengles20tutorial;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class Img {
	private final String VertexShaderCode =
	        // This matrix member variable provides a hook to manipulate
	        // the coordinates of the objects that use this vertex shader
	        "uniform mat4 uMVPMatrix;" +

	        "attribute vec4 vPosition;" +
	        "void main() {" +
	        // the matrix must be included as a modifier of gl_Position
	        "  gl_Position = uMVPMatrix * vPosition;" +
	        "}";

	private final String FragmentShaderCode =
	        "precision mediump float;" +
	        "uniform vec4 vColor;" +
	        "void main() {" +
	        "  gl_FragColor = vColor;" +
	        "}";

	private final FloatBuffer VertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3;
	static float LineCoords[] = {1000.5f, 1000.5f, 0.0f, 0.0f, 0.0f, 1000.5f };

	private final int VertexCount = LineCoords.length / COORDS_PER_VERTEX;
	private final int VertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

	// Set color with red, green, blue and alpha (opacity) values
	float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

	public Img(){
	    // initialize vertex byte buffer for shape coordinates
	    ByteBuffer bb = ByteBuffer.allocateDirect(
	            // (number of coordinate values * 4 bytes per float)
	            LineCoords.length * 4);
	    // use the device hardware's native byte order
	    bb.order(ByteOrder.nativeOrder());

	    // create a floating point buffer from the ByteBuffer
	    VertexBuffer = bb.asFloatBuffer();
	    // add the coordinates to the FloatBuffer
	    VertexBuffer.put(LineCoords);
	    // set the buffer to read the first coordinate
	    VertexBuffer.position(0);


	    int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, VertexShaderCode);
	    int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, FragmentShaderCode);

	    mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
	    GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables

	}

	public void SetVerts(float v0, float v1, float v2, float v3, float v4, float v5)
	{
	    LineCoords[0] = v0;
	    LineCoords[1] = v1;
	    LineCoords[2] = v2;
	    LineCoords[3] = v3;
	    LineCoords[4] = v4;
	    LineCoords[5] = v5;

	    VertexBuffer.put(LineCoords);
	    // set the buffer to read the first coordinate
	    VertexBuffer.position(0);

	}

	public void SetColor(float red, float green, float blue, float alpha)
	{
	    color[0] = red;
	    color[1] = green;
	    color[2] = blue;
	    color[3] = alpha;
	}

	public void draw(float[] mvpMatrix) {
	    // Add program to OpenGL ES environment
	    GLES20.glUseProgram(mProgram);

	    // get handle to vertex shader's vPosition member
	    mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

	    // Enable a handle to the triangle vertices
	    GLES20.glEnableVertexAttribArray(mPositionHandle);

	    // Prepare the triangle coordinate data
	    GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
	                                 GLES20.GL_FLOAT, false,
	                                 VertexStride, VertexBuffer);

	    // get handle to fragment shader's vColor member
	    mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

	    // Set color for drawing the triangle
	    GLES20.glUniform4fv(mColorHandle, 1, color, 0);

	    // get handle to shape's transformation matrix
	    mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	    //MyGLRenderer.checkGlError("glGetUniformLocation");

	    // Apply the projection and view transformation
	    GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
	    //MyGLRenderer.checkGlError("glUniformMatrix4fv");


	    // Draw the triangle
	    GLES20.glDrawArrays(GLES20.GL_LINES, 0, VertexCount);

	    // Disable vertex array
	    GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
}
