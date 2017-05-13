package com.example.opengles20_projection;

//import java.io.InputStream;

import android.app.Activity;
//import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private MyGLSurfaceView mGLView;
	private Button swapBtn;
	private Button up, down, left, right, near, far;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mGLView = (MyGLSurfaceView)findViewById(R.id.surfaceviewclass);
        
        final MyGLRenderer r = mGLView.getRenderer();
        
        swapBtn = (Button)findViewById(R.id.swapBtn);
        up = (Button)findViewById(R.id.Up);
        down = (Button)findViewById(R.id.Down);
        left = (Button)findViewById(R.id.Left);
        right = (Button)findViewById(R.id.Right);
        near = (Button)findViewById(R.id.Near);
        far = (Button)findViewById(R.id.Far);
        
        View.OnClickListener swapTheView = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				r.setViewMode();
			}
			
        };
        View.OnTouchListener CameraDownHandler = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					r.camDown = true;
				if(event.getAction() == MotionEvent.ACTION_UP)
					r.camDown = false;
				return true;
				
			}
        };
        View.OnTouchListener CameraUpHandler = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					r.camUp = true;
				if(event.getAction() == MotionEvent.ACTION_UP)	
					r.camUp = false;
				return true;
			}
        };
        View.OnTouchListener CameraLeftHandler = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					r.camLeft = true;
					//r.isRotatingDown = true;
				if(event.getAction() == MotionEvent.ACTION_UP)	
					r.camLeft = false;
				return true;
			}
        };
        View.OnTouchListener CameraRightHandler = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					r.camRight = true;
				if(event.getAction() == MotionEvent.ACTION_UP)	
					r.camRight = false;
				return true;
			}
        };
        View.OnTouchListener CameraInHandler = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					r.camIn = true;
				if(event.getAction() == MotionEvent.ACTION_UP)
					r.camIn = false;
				return true;
				
			}
        };
        View.OnTouchListener CameraOutHandler = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					r.camOut = true;
				if(event.getAction() == MotionEvent.ACTION_UP)
					r.camOut = false;
				return true;
				
			}
        };
        up.setOnTouchListener(CameraUpHandler);
        down.setOnTouchListener(CameraDownHandler);
        left.setOnTouchListener(CameraLeftHandler);
        right.setOnTouchListener(CameraRightHandler);
        near.setOnTouchListener(CameraInHandler);
        far.setOnTouchListener(CameraOutHandler);
        
        swapBtn.setOnClickListener(swapTheView);
        
        final DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		MyGLRenderer.y = dm.heightPixels;
		MyGLRenderer.x = dm.widthPixels;
		
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
