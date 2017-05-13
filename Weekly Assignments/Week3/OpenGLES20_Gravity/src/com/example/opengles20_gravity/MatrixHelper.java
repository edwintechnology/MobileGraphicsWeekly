package com.example.opengles20_gravity;

import android.util.DisplayMetrics;

/**
 * Created by Todd on 1/5/14.
 */
public class MatrixHelper {

	
	public static void ortho(float[] m, float x , float y){		
		for(int i = 0; i < 16; i ++)
			m[i] = 0.0f;
		
		m[0] = 2.0f/x;
		m[5] = 2.0f/y;
		m[10] = 1.0f;
		m[15] = 1.0f;
		
	}
    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2));

        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }
}