package com.example.opengles20tutorial;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private MyGLSurfaceView mGLView;
	private Button swapBtn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        //mGLView = new MyGLSurfaceView(this);
        

        mGLView = (MyGLSurfaceView)findViewById(R.id.surfaceviewclass);
        
        final MyGLRenderer r = ((MyGLSurfaceView) mGLView).getRenderer();
        
        swapBtn = (Button)findViewById(R.id.swapview);
        
        View.OnClickListener swapTheView = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				r.setViewMode();
			}
			
        };
        swapBtn.setOnClickListener(swapTheView);
        
        FileIO file = new FileIO();
        AssetManager assetMgr = this.getAssets();
        try{
        	InputStream in = assetMgr.open("img3.dat");
        	Vertices v = file.ReadFile(in, 1, 1, 1);
        	Img.LineCoords = v.ToArray();
        }
        catch(Exception e){}
        
		final DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		MyGLRenderer.y = dm.heightPixels;
		MyGLRenderer.x = dm.widthPixels;
       // FileIO file = new FileIO("img1.dat");
 	    //Vertices v = file.ReadImgFile();
 	    //v.Print();
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
	
	
    public MyGLSurfaceView(Context context){
        super(context);
        setEGLContextClientVersion(2);
        // Set the Renderer for drawing on the GLSurfaceView
        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    
    
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;
    private final MyGLRenderer mRenderer;
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                mRenderer.setAngle(
                        mRenderer.getAngle() +
                        ((dx + dy) * TOUCH_SCALE_FACTOR));  // = 180.0f / 320
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
    
    public MyGLRenderer getRenderer()
    {
    	return mRenderer;
    }
}

