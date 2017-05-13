package com.example.opengles20_gyroscope;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileIO {
	
	public Vertices ReadFile(InputStream in, float xValue, float yValue, float zValue)
	{
		Vertices v = new Vertices();
		//File file = new File(filename);
		try{
		//StringTokenizer st = null;
		//InputStream in = context.getAssets().open(filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line;
		float x, y, z;
		while((line = br.readLine()) != null){
			String[] splitline = line.trim().split(" ");
			if(splitline[0].equalsIgnoreCase("J"))
			{
				continue;
			}
			else
			{
				x = Float.parseFloat(splitline[0]) / xValue;
				y = Float.parseFloat(splitline[1]) / yValue;
				z = Float.parseFloat(splitline[2]) / zValue;
			}
			v.add(new Vertex(x,y,z));				
		}
		br.close();
		in.close();
		}
		catch(Exception e){ e.printStackTrace();}
		return v;
	}
	
	public FileIO()
	{
		
	}
	
}
