package com.example.touchgesturecube;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.widget.TextView;


public class OpenGLES20Basic extends Activity {
	private GLSurfaceView mGLView;
	private MyGLRenderer r;
	private GestureDetectorCompat gestureDetector; // detect common gestures
	private ScaleGestureDetector scaleGestureDetector; // detect pinch gestures
	private float density;
	private float prevVelocityX, prevVelocityY;

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity
		mGLView = new MyGLSurfaceView(this);
		setContentView(mGLView);
		
		// Instantiate the gesture detector with the application context 
		// and an implementation of SimpleOnGestureListener
		gestureDetector = new GestureDetectorCompat(this, new MyGestureListener());

		// Instantiate the scale gesture detector with the application context 
		// and an implementation of SimpleOnScaleGestureListener
		scaleGestureDetector = new ScaleGestureDetector(this, new MySimpleOnScaleGestureListener());
		
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		density = displayMetrics.density;

	}

	@Override
	protected void onPause() {
	    super.onPause();
	    // The following call pauses the rendering thread.
	    // If your OpenGL application is memory intensive,
	    // you should consider de-allocating objects that
	    // consume significant memory here.
	    mGLView.onPause();
	}
	    
	@Override
	protected void onResume() {
		super.onResume();
		// The following call resumes a paused rendering thread.
		// If you de-allocated graphic objects for onPause()
		// this is a good place to re-allocate them.
		mGLView.onResume();
	}
	    

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getPointerCount() > 1) {
			scaleGestureDetector.onTouchEvent(event);
		} else {
			gestureDetector.onTouchEvent(event);
		}
		return true;
	}

		/**
		 * Gesture detector
		 * @author Todd
		 *
		 */
	class MyGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onDown(MotionEvent event) {
			return true;
		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2,
			float velocityX, float velocityY) {
			System.out.println(velocityX);
			r.velocityX = velocityX/density/1000f;
			r.velocityY = velocityY/density/1000f;
			if(velocityX>0)
				r.vXDir = 1;
			else if(velocityX<0)
				r.vXDir = -1;
			else 
				r.vXDir = 0;
			
			if(velocityY>0)
				r.vYDir = 1;
			else if(velocityY<0)
				r.vYDir = -1;
			else 
				r.vYDir = 0;
			
			r.accelerationX = 0.1f/density;
			r.accelerationY = 0.1f/density;
			return true;
		}

		@Override
		public void onLongPress(MotionEvent event) {
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
			float distanceX, float distanceY) {
			float rotX = -distanceX /  density / 2.0f;
			float rotY = -distanceY /  density / 2.0f;
			r.onRotate(rotX, rotY);
			return true;
		}

		@Override
		public void onShowPress(MotionEvent event) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent event) {
			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent event) {
			return true;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent event) {
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent event) {
			return true;
		}
	}
	
	class MyGLSurfaceView extends GLSurfaceView {

		public MyGLSurfaceView(Context context) {
			super(context);

		    // Create an OpenGL ES 2.0 context.
		    setEGLContextClientVersion(2);

		    r = new MyGLRenderer();
		    
		    // Set the Renderer for drawing on the GLSurfaceView
		    setRenderer(r);

		    // Render the view only when there is a change in the drawing data
		    //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		}
	}
	
	/**
	 * Scale gesture detector
	 * @author Todd
	 *
	 */
	public class MySimpleOnScaleGestureListener extends	SimpleOnScaleGestureListener {

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			float scale = 1/detector.getScaleFactor();
			if(scale >1.0 && r.scale > .01){
				r.scale += ((1.0f-scale)/density);
			}
			if(scale<1.0  && r.scale < 5){
				r.scale += ((1.0f - scale)/density);
			}
			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {

		}
	}

}

