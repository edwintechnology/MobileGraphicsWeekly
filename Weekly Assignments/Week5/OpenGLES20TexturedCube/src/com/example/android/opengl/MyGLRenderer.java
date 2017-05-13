package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Cube mCube;    
    private Context context;
    
    public MyGLRenderer(Context context) {
    	this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                
        // Enable depth test for HSR
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        mCube = new Cube(context);
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
        
        // Create a perspective projection
        mCube.createPerspectiveProjection(width, height);    
    }

    public void switchTexture() {
    	mCube.switchTexture();
    }
}
