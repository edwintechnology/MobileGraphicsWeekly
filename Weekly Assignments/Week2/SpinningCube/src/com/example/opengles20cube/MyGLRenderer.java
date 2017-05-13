package com.example.opengles20cube;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;


public class MyGLRenderer implements GLSurfaceView.Renderer{

    private Cube mCube;
        
    static float[] projectionMatrix = new float[16];
    static float[] viewMatrix = new float[16];
    static float[] modelMatrix = new float[16];
    static int uModelLocation;
    static int uViewLocation;
    static int uProjectionLocation;
    
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                
        // Enable depth test for HSR
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        

        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.setIdentityM(projectionMatrix, 0);
        Matrix.setIdentityM(modelMatrix, 0);

        mCube = new Cube();
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Draw background color and clear depth buffer bit for HSR
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        // Draw cube
        mCube.draw();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes, such as screen rotation
        GLES20.glViewport(0, 0, width, height);
        
        float ratio = (float) width / (float) height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.perspectiveM(projectionMatrix, 0, 45.0f, ratio, 1, 10);     
    }

    public static int loadShader(int type, String shaderCode){
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
     
        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0)
        {
        	final IntBuffer info_len = IntBuffer.allocate(1);
        	GLES20.glGetShaderiv(shader, GLES20.GL_INFO_LOG_LENGTH, info_len);
        	String log = null;
        	if(info_len.get() > 0){
        		log = GLES20.glGetShaderInfoLog(shader);
        	}
            GLES20.glDeleteShader(shader);
            shader = 0;
            throw new RuntimeException(log + " Error creating shader.");
        }

        return shader;
    }
}


class Cube {

    private final String vertexShaderCode =
    	"uniform mat4 u_Projection;" +
    	"uniform mat4 u_View;"		 +
    	"uniform mat4 u_Model;"		 +
        "attribute vec4 a_Position;"  +
        "void main() {" +
        "  mat4 m_ModelView = u_View * u_Model;"		+
        "  mat4 m_MVP = u_Projection * m_ModelView;"	+
        "  gl_Position = m_MVP * a_Position;"			+
        "}";

    private final String fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 v_Color;" +
        "void main() {" +
        "  gl_FragColor = v_Color;" +
        "}";

    private final FloatBuffer vertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    
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

    
    public void draw() {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);
        
        // rotate the cube
        rotate(); 
        
        // get the location of the model matrix uniform
        MyGLRenderer.uModelLocation = GLES20.glGetUniformLocation(mProgram, "u_Model");
        
        // send the model matrix to the shader
        GLES20.glUniformMatrix4fv(MyGLRenderer.uModelLocation, 1, false, MyGLRenderer.modelMatrix, 0);
            
        // get the location of the view matrix uniform
        MyGLRenderer.uViewLocation = GLES20.glGetUniformLocation(mProgram, "u_View");
        
        // send the view matrix to the shader
        GLES20.glUniformMatrix4fv(MyGLRenderer.uViewLocation, 1, false, MyGLRenderer.viewMatrix, 0);
       
        // get the location of the projection matrix uniform
        MyGLRenderer.uProjectionLocation = GLES20.glGetUniformLocation(mProgram, "u_Projection");
        
        // send the projection matrix to the shader
        GLES20.glUniformMatrix4fv(MyGLRenderer.uProjectionLocation, 1, false, MyGLRenderer.projectionMatrix, 0);
        
        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "v_Color");

        // Draw each of the faces of the cube with a different color
        for(int i = 0; i < CUBE_FACES; i++) {
	        // Set color for drawing the side of the cube
        	GLES20.glUniform4fv(mColorHandle, 1, color[i], 0);
	        
	        // Draw the face
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, i * CUBE_FACES, vertexCount / CUBE_FACES);
        }
        
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
    
    
    public void rotate() {
        // translation
        Matrix.setIdentityM(MyGLRenderer.modelMatrix, 0);
        Matrix.translateM(MyGLRenderer.modelMatrix, 0, 0f, 0f, -4f);
        
        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;        
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        
        // rotation (x and y axis)
        Matrix.rotateM(MyGLRenderer.modelMatrix, 0, angleInDegrees, 1f, 1f, 0f);
        
    }
}
