package com.example.opengles20_accelo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.*;

public class MyGLRenderer implements GLSurfaceView.Renderer {

	public static Img mImg;
	private Cube mCube;
	
	private final float[] mMVPMatrix = new float[16];
	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];
	private final float[] mTransformationMatrix = new float[16];

	private float mAngle;
    
	public static float x = 1;
	public static float y = 1;
	
	public boolean viewMode = true;
    
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                
        // Enable depth test for HSR
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        mImg = new Img();
        mCube = new Cube();
    }
    public void setViewMode()
    {
    	viewMode = !viewMode;
    }
    
    @Override
    public void onDrawFrame(GL10 unused) {
    	float[] scratch = new float[16];
        // Draw background color and clear depth buffer bit for HSR
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        Matrix.setIdentityM(mViewMatrix, 0);
       
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        
        
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.setIdentityM(mTransformationMatrix, 0);
        Matrix.translateM(mTransformationMatrix, 0, MainActivity.x, MainActivity.y, MainActivity.z);
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mTransformationMatrix, 0);
        // Draw square
        //mImg.draw(scratch);
        // Draw triangle
        mCube.draw(scratch);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
    	// Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);
        
        //MatrixHelper.ortho(mProjectionMatrix,x,y);
        //unused.glOrthof(-2200, 2200, -2200, 2200, -1, 1);
        float ratio = (float) width / height;
       
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        
        Matrix.setIdentityM(mProjectionMatrix, 0);
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 2, 7);
        // Create a perspective projection
        //MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);     
    }

    public static int loadShader(int type, String shaderCode){
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }
}