package com.example.opengles20_sensorcheck;

import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView text;
	SensorManager mSensorManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
		text = (TextView)findViewById(R.id.textview);

		StringBuilder sensors = new StringBuilder();
		List<Sensor> list = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		for(Sensor sensor: list){
			sensors.append(sensor.getName() + "\n");
			sensors.append(sensor.getVendor() + "\n");
			sensors.append(sensor.getVersion() + "\n\n");
		}
						
		text.setText(sensors.toString());
	}
}

