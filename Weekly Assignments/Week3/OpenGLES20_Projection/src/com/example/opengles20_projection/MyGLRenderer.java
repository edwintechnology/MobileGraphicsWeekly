package com.example.opengles20_projection;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.annotation.SuppressLint;
import android.opengl.*;

public class MyGLRenderer implements GLSurfaceView.Renderer {

	public static Img mImg;
	private Cube mCube;
	private Sphere mSphere;
	
	static final float[] mModelMatrix = new float[16];
	static final float[] mProjectionMatrix = new float[16];
	static final float[] mViewMatrix = new float[16];
	static final float[] mRotationMatrix = new float[16];
	
	private float mAngle;
    
	public static float x = 1;
	public static float y = 1;
	
	public static float camX, camY, camZ = -3;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                
        // Enable depth test for HSR
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
   
        mImg = new Img();
        mCube = new Cube();
        mSphere = new Sphere(1, 25);
    }
    private int viewMode = 0;
    public void setViewMode()
    {
    	if(viewMode < 2)
    		viewMode++;
    	else
    		viewMode = 0;
    }
    
    @SuppressLint("NewApi")
	@Override
    public void onDrawFrame(GL10 unused) {
    	float[] scratch = new float[16];
        // Draw background color and clear depth buffer bit for HSR
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setIdentityM(mProjectionMatrix, 0);
        
        float ratio = (float) x / y;
        
        if(viewMode == 0)
        	Matrix.orthoM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 2, 7);
       
        else if(viewMode == 1)
        	Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 2, 7);
       
        else if(viewMode == 2)
        	Matrix.perspectiveM(mProjectionMatrix, 0, 60.0f, ratio, 2, 100.0f);
        
        
        cameraMove();
        Matrix.setLookAtM(mViewMatrix, 0, camX, camY, camZ, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        
        // Calculate the projection and view transformation
        //Matrix.multiplyMM(mModelMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0.0f, 1.0f, 1.0f);
        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mModelMatrix, 0, mRotationMatrix, 0);
        // Draw square
        //mImg.draw(scratch);
        // Draw triangle
        System.arraycopy(scratch, 0, mModelMatrix, 0, scratch.length);
        //mCube.draw();
        mSphere.draw();
    }
    public boolean camRight = false, camLeft = false, camUp = false, camDown = false, camIn = false, camOut = false;
    
    public void cameraMove() {
    	if(camRight)
    	{
    		camX = camX + .01f;
    	}
    	else if(camLeft)
    	{
    		camX = camX - .01f;
    	}
    	else if(camUp)
    	{
    		camY = camY + .01f;
    	}
    	else if(camDown)
    	{
    		camY = camY - .01f;
    	}
    	else if(camIn)
    	{
    		camZ = camZ - .01f;
    	}
    	else if(camOut)
    	{
    		camZ = camZ + .01f;
    	}
    }
    
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        
        float ratio = (float) width / height;
       
        Matrix.setIdentityM(mProjectionMatrix, 0);
    	Matrix.orthoM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 2, 7);
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