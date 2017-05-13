package com.example.opengles20_gravity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{

	private MyGLSurfaceView mGLView;
	
	TextView text;
	TextView raw;
	TextView time;
	
	static float x, y, z;
	
	private SensorManager mSensorManager;
	private Sensor mSensor;
    private float mSensorX;
    private float mSensorY;
    private long mSensorTimeStamp;
    private Display mDisplay;
    private WindowManager mWindowManager;
    private boolean mInit;
    private final static float WHITENOISE = 2.0f;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
		//setContentView(R.layout.activity_main);
       
		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		mDisplay = mWindowManager.getDefaultDisplay();
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	public void onSensorChanged(SensorEvent event){
		if (event.sensor.getType() != Sensor.TYPE_GRAVITY)
            return;
		
		//x = event.values[0];
		//y = event.values[1];
		//z = event.values[2];	
		
		switch (mDisplay.getRotation()) {
        case Surface.ROTATION_0:
            x = event.values[0] / 10.0f;
            y = event.values[1] / 10.0f;
            break;
        case Surface.ROTATION_90:
            x = -event.values[1] / 10.0f;
            y = event.values[0] / 10.f;
            break;
        case Surface.ROTATION_180:
            x = -event.values[0] / 10.0f;
            y = -event.values[1] / 10.0f;
            break;
        case Surface.ROTATION_270:
            x = event.values[1] / 10.0f;
            y = -event.values[0] / 10.0f;
            break;
            

    }
		if(x >= 1.0f) 
			x = 1.0f;
		if(y >= 1.0f) 
			y = 1.0f;
		if(x <= -1.0f)
			x = -1.0f;
		if(y <= -1.0f)
			y = -1.0f;
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	class MyGLSurfaceView extends GLSurfaceView {
		
		
	    public MyGLSurfaceView(Context context){
	        super(context);
	        setEGLContextClientVersion(2);
	        // Set the Renderer for drawing on the GLSurfaceView
	        // Set the Renderer for drawing on the GLSurfaceView
	        mRenderer = new MyGLRenderer();
	        setRenderer(mRenderer);
	      //  setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
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
}