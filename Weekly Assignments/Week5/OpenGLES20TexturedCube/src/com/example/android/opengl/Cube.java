package com.example.android.opengl;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import util.MatrixHelper;
import util.TextResourceReader;
import util.TextureHelper;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class Cube {
    
    private float cubeCoords[] = { // in counterclockwise order: (x, y, z, w, s, t)
			// front face
			0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f,			// top right
			-0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f,		// bottom left
			0.5f, -0.5f, 0.5f, 1.0f, 1.0f, 0.0f,		// bottom right

			0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 		// top right
			-0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f,		// top left
			-0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 		// bottom left

			// left face
			-0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 		// top right
			-0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 		// bottom left
			-0.5f, -0.5f, 0.5f, 1.0f, 1.0f, 0.0f, 		// bottom right

			-0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f,		// top right
			-0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 1.0f, 		// top left
			-0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f,		// bottom left

			// back face
			-0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 		// top right
			0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 		// bottom left
			-0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f, 		// bottom right

			-0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 		// top right
			0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 1.0f, 		// top left
			0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 		// bottom left

			// right face
			0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 		// top right
			0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 		// bottom left
			0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f, 		// bottom right

			0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 		// top right
			0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f, 		// top left
			0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 		// bottom left

			// top face
			0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 		// top right
			-0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 		// bottom left
			0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 0.0f, 		// bottom right

			0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 		// top right
			-0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 1.0f, 		// top left
			-0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 		// bottom left

			// bottom face
			0.5f, -0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 		// top right
			-0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 		// bottom left
			0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f, 		// bottom right

			0.5f, -0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 		// top right
			-0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 1.0f, 		// top left
			-0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f 		// bottom left
	};
        
    public static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMPONENT_COUNT = 4;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    
    // Number of coordinates per vertex in this array
    private final int COORDS_PER_VERTEX = POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT;
    
    // Number of vertices in object
    private final int VERTEX_COUNT = cubeCoords.length / COORDS_PER_VERTEX;
    
    private int current_texture;
    private int texture_lava;
    private int texture_paper;
    
    private float[] projectionMatrix = new float[16];
    private float[] modelMatrix = new float[16];
    
    private final FloatBuffer vertexBuffer;
    private final int mProgram;
        
    // Uniform locations
    private int uMatrixLocation;
    private int uTextureUnitLocation;

    // Attribute locations
    private int aPositionLocation;
    private int aTextureCoordinatesLocation;
        
    private float current_rotation = 0f;
    private final float ROTATION_CHANGE = 1f;
    

    public Cube(Context context) {
        // Initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		cubeCoords.length * 4);
        // Use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // Create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // Add the coordinates to the FloatBuffer
        vertexBuffer.put(cubeCoords);
        // Set the buffer to read the first coordinate
        vertexBuffer.position(0);
        
        // Load the textures
        texture_lava = TextureHelper.loadTexture(context, R.drawable.lava);
        texture_paper = TextureHelper.loadTexture(context, R.drawable.paper);
        current_texture = texture_lava;
                
        // Read in the shader source code from file
        String vertexShaderCode = TextResourceReader
                .readTextFileFromResource(context, R.raw.texture_vertex_shader);
        String fragmentShaderCode = TextResourceReader
                .readTextFileFromResource(context, R.raw.texture_fragment_shader);
                
        // Load the shaders
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
                
        // Create the shader program
        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program
        
        // Retrieve uniform locations for the shader program
        uMatrixLocation = glGetUniformLocation(mProgram, "u_Matrix");
        uTextureUnitLocation = glGetUniformLocation(mProgram, "u_TextureUnit");

        // Retrieve attribute locations for the shader program
        aPositionLocation = glGetAttribLocation(mProgram, "a_Position");
        aTextureCoordinatesLocation = glGetAttribLocation(mProgram, "a_TextureCoordinates");
    }
    
    
    public void draw() {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);
        
        // Rotate the cube
        rotate(); 
        
        // Pass the matrix into the shader program
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, modelMatrix, 0);

        // Set the active texture unit to texture unit 0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, current_texture);

        // Tell the texture uniform sampler to use this texture in the shader
        // by telling it to read from texture unit 0
        GLES20.glUniform1i(uTextureUnitLocation, 0);
        
        // Prepare the vertex data
        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, vertexBuffer);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
        vertexBuffer.position(0);
                        
        // Prepare the texture coordinate data
        vertexBuffer.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aTextureCoordinatesLocation, TEXTURE_COORDINATES_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, vertexBuffer);
        GLES20.glEnableVertexAttribArray(aTextureCoordinatesLocation);
        vertexBuffer.position(0);
        
        // Draw the cube
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTEX_COUNT);
        
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(aPositionLocation);
        GLES20.glDisableVertexAttribArray(aTextureCoordinatesLocation);
    }
    
    
    public void rotate() {
        // Translation
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -4f);
        
        // Update the amount to rotate
        current_rotation = current_rotation >= 360 ? 0 : current_rotation + ROTATION_CHANGE;
        
        // Rotation (x and y axis)
        Matrix.rotateM(modelMatrix, 0, current_rotation, 1f, 1f, 0f);
        
        // Multiply the model and projection matrices
        final float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, modelMatrix, 0, temp.length);
    }
    
    
    public int loadShader(int type, String shaderCode){
        // Create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // Add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
    
        
    public void createPerspectiveProjection(int width, int height) {
    	MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
    }
    
    
    public void switchTexture() {
    	current_texture = current_texture == texture_lava ? texture_paper : texture_lava;
    }
}

