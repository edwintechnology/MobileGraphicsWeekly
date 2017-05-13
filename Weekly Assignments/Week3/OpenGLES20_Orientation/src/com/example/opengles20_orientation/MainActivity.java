package com.example.opengles20_orientation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;

public class MainActivity extends Activity implements SensorEventListener{

	private MyGLSurfaceView mGLView;
	private SensorManager mSensorManager;
	private Sensor gSensor;
	private Sensor mSensor;
	private Sensor aSensor;
	private Sensor rSensor;
	private Sensor lSensor;
	private Sensor gySensor;
	
	private static final float DEG = 57.2957795f;
	private static final float kFilteringFactor = 0.01f;
	    
	static float[] magn = new float[3];
	static float[] grav = new float[3];
	static float[] accel = new float[3];
	
	static float[] orientation = new float[3];
	
	private boolean hasM, hasG, hasA = false;
	
	float[] Rmat = new float[9];
    float[] R2 = new float[9];
    float[] Imat = new float[9];
    
    static float pitch, row, yaw;
    
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
		//setContentView(R.layout.activity_main);
       
        /* add all of the sensors */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        
		gSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		mSensorManager.registerListener(this, gSensor, SensorManager.SENSOR_DELAY_GAME);
        
		aSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_GAME);
		
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
	}

	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch( event.sensor.getType() ) {
        case Sensor.TYPE_GRAVITY:
          grav[0] = event.values[0];
          grav[1] = event.values[1];
          grav[2] = event.values[2];
          hasG = true;
          break;
        case Sensor.TYPE_ACCELEROMETER:
          if (hasG) break;    // don't need it, we have better
          accel[0] = (event.values[0] * kFilteringFactor + accel[0] * (1.0f - kFilteringFactor));
          accel[1] = (event.values[1] * kFilteringFactor + accel[1] * (1.0f - kFilteringFactor));
          accel[2] = (event.values[2] * kFilteringFactor + accel[2] * (1.0f - kFilteringFactor));
          hasA = true;
          break;
        case Sensor.TYPE_MAGNETIC_FIELD:
          magn[0] = event.values[0];
          magn[1] = event.values[1];
          magn[2] = event.values[2];
          hasM = true;
          break;
        default:
          return;
      }
		if (hasG && hasM) {
            SensorManager.getRotationMatrix(Rmat, Imat, grav, magn);
            SensorManager.remapCoordinateSystem(Rmat,
                    SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, R2);
            // Orientation isn't as useful as a rotation matrix, but
            // we'll show it here anyway.
            SensorManager.getOrientation(R2, orientation);
            pitch = orientation[1] * DEG;
    		row = orientation[2] * DEG;
    		yaw = orientation[0] * DEG;
		}
		else if(!hasG && hasA && hasM)
		{
			 SensorManager.getRotationMatrix(Rmat, Imat, accel, magn);
	            SensorManager.remapCoordinateSystem(Rmat,
	                    SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, R2);
	            // Orientation isn't as useful as a rotation matrix, but
	            // we'll show it here anyway.
	            SensorManager.getOrientation(R2, orientation);
	            pitch = orientation[1] * DEG;
	    		row = orientation[2] * DEG;
	    		yaw = orientation[0] * DEG;
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
