package com.example.opengles20tutorial;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Model {
	private FloatBuffer v;
	private FloatBuffer vt;
	private FloatBuffer vn;
	private int v_size;
	
	public void fill(ArrayList<Face> faces, boolean has_tex, boolean has_normals)
	{
		int f_len = faces.size();
		
		this.v_size = f_len * 3;
		this.v = FloatBuffer.allocate(v_size * 3);
		
		if(has_tex)
			this.vt = FloatBuffer.allocate(v_size * 2);
		if(has_normals)
			this.vn = FloatBuffer.allocate(v_size * 3);
		
		for(int i = 0; i < f_len; i++){
			Face face = faces.get(i);
			face.pushOnto(this.v, this.vt, this.vn);
		}
		
		 this.v.rewind();
         if (this.vt != null)
             this.vt.rewind();
         if (this.vn != null)
             this.vn.rewind();
	}
	public class ObjModel
	{
		
	}
}
