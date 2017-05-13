package com.example.opengles20_gyroscope;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

public class MainActivity extends Activity implements SensorEventListener{

	private MyGLSurfaceView mGLView;

	static float x, y, z;
	
	private SensorManager mSensorManager;
	private Sensor mSensorGyro;

    static float[] gyro = new float[3];
    static float[] gyroOrientation = new float[3];
    static float[] magnet = new float[3];
    static float[] accel = new float[3];
 
    // accelerometer and magnetometer based rotation matrix
    private float timestamp;
    private float[] deltaRotationVector = new float[4];
    public static final float EPSILON = 0.000000001f;
    private static final float NS2S = 1.0f / 1000000000.0f;
    static boolean gyroExists = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);

        mSensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
		mSensorGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mSensorManager.registerListener(this, mSensorGyro, SensorManager.SENSOR_DELAY_GAME);
	}
	
	@Override
	protected void onPause()
	{
		mSensorManager.unregisterListener(this);
	}
	
	//@Override
	//protected void onResume()
	//{
	//	mSensorManager.registerListener(this, mSensorGyro, SensorManager.SENSOR_DELAY_GAME);
	//	mSensorManager.registerListener(this, mSensorAccel, SensorManager.SENSOR_DELAY_GAME);
	//	mSensorManager.registerListener(this, mSensorMagn, SensorManager.SENSOR_DELAY_GAME);
	//}
	
	@SuppressLint("NewApi")
	public void onSensorChanged(SensorEvent event){
		if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            // Axis of the rotation sample, not normalized yet.
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            float omegaMagnitude = (float) Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;
            }

            float thetaOverTwo = omegaMagnitude * dT / 2.0f;
            float sinThetaOverTwo = (float)Math.sin(thetaOverTwo);
            float cosThetaOverTwo = (float)Math.cos(thetaOverTwo);
            deltaRotationVector[0] = sinThetaOverTwo * axisX;
            deltaRotationVector[1] = sinThetaOverTwo * axisY;
            deltaRotationVector[2] = sinThetaOverTwo * axisZ;
            deltaRotationVector[3] = cosThetaOverTwo;
        }
        timestamp = event.timestamp;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
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
