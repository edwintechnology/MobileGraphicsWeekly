package com.example.opengles20_accelo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends Activity implements SensorEventListener{

	private MyGLSurfaceView mGLView;
	
	private static final float kFilteringFactor = 0.01f;
	static float x, y, z;
	
	private SensorManager mSensorManager;
	private Sensor mSensor;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	public void onSensorChanged(SensorEvent event){
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

		x = (event.values[0] * kFilteringFactor + x * (1.0f - kFilteringFactor)); 
        y = (event.values[1] * kFilteringFactor + y * (1.0f - kFilteringFactor));
        z = 0;//(event.values[2] * kFilteringFactor + z * (1.0f - kFilteringFactor));
        
		
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
