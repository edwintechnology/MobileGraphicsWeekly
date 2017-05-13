package com.example.androidmenucube;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class OpenGLES20Basic extends Activity {

	  private MyGLSurfaceView mGLView;
	  private Button rotateRightButton;
	  private Button rotateLeftButton;
	  private Button rotateUpButton;
	  private Button rotateDownButton;
	  private SeekBar brightnessBar;
	  

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        mGLView = (MyGLSurfaceView)findViewById(R.id.surfaceviewclass);
	        
	        
	        
	        final MyGLRenderer r = mGLView.getRenderer();
	        rotateRightButton = (Button)findViewById(R.id.rotateright);
	        rotateLeftButton = (Button)findViewById(R.id.rotateleft);
	        rotateUpButton = (Button)findViewById(R.id.rotateup);
	        rotateDownButton = (Button)findViewById(R.id.rotatedown);
	        
	        brightnessBar = (SeekBar)findViewById(R.id.brightnessBar);
	        brightnessBar.setProgress(100);
	        
	        SeekBar.OnSeekBarChangeListener brightnessBarHandler = new SeekBar.OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
						r.brightness = (float)progress/100.0f;
						System.out.println(progress);
					
				}
			};

	        View.OnTouchListener rotateRightHandler = new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if(event.getAction() == event.ACTION_DOWN)
						r.rotX=0;
						r.isRotatingRight = true;
					if(event.getAction() == event.ACTION_UP)	
						r.isRotatingRight = false;
					return true;
				}
	        };
	        View.OnTouchListener rotateLeftHandler = new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if(event.getAction() == event.ACTION_DOWN)
						r.rotX=0;
						r.isRotatingLeft = true;
					if(event.getAction() == event.ACTION_UP)
						r.isRotatingLeft = false;
					return true;
				}
	        };
	        
	        View.OnTouchListener rotateUpHandler = new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if(event.getAction() == event.ACTION_DOWN)
						r.rotY=0;
						r.isRotatingUp = true;
					if(event.getAction() == event.ACTION_UP)	
						r.isRotatingUp = false;
					return true;
				}
	        };
	        
	        View.OnTouchListener rotateDownHandler = new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if(event.getAction() == event.ACTION_DOWN)
						r.rotY=0;
						r.isRotatingDown = true;
					if(event.getAction() == event.ACTION_UP)	
						r.isRotatingDown = false;
					return true;
				}
	        };
	        

	        rotateRightButton.setOnTouchListener(rotateRightHandler);

	        rotateLeftButton.setOnTouchListener(rotateLeftHandler);
	        
	        rotateUpButton.setOnTouchListener(rotateUpHandler);

	        rotateDownButton.setOnTouchListener(rotateDownHandler);
	        
	        brightnessBar.setOnSeekBarChangeListener(brightnessBarHandler);
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

	}

class MyGLSurfaceView extends GLSurfaceView {
	MyGLRenderer r;
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
	public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        r = new MyGLRenderer();
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(r);

        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
	
	public MyGLRenderer getRenderer(){
		return r;
	}
    
}


