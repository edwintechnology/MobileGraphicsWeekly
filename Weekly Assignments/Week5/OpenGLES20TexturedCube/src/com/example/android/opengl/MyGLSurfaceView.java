package com.example.android.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class MyGLSurfaceView extends GLSurfaceView {

	MyGLRenderer renderer;
	private GestureDetector gestureDetector; // detect common gestures
	
    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        
        renderer = new MyGLRenderer(context);
        
        gestureDetector = new GestureDetector(context, new MyGestureListener());

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);

        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	gestureDetector.onTouchEvent(event);
		return true;
	}
    
    
    class MyGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onDown(MotionEvent event) {
			renderer.switchTexture();
			return true;
		}
	}
}
