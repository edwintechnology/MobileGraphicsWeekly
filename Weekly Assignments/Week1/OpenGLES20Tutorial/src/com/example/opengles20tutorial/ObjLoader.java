package com.example.opengles20tutorial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ObjLoader {

	private static ArrayList<Vertex> v = new ArrayList<Vertex>();
	private static ArrayList<Vertex> vt = new ArrayList<Vertex>();
	private static ArrayList<Vertex> vn = new ArrayList<Vertex>();
	private static ArrayList<Face> f = new ArrayList<Face>();
	private static ArrayList<Model> o = new ArrayList<Model>();
	
	public static ArrayList<Vertex> getV()
	{
		return v;
	}
	public static ArrayList<Vertex> getVT()
	{
		return vt;
	}
	
	public static ArrayList<Vertex> getVN()
	{
		return vn;
	}
	public static ArrayList<Face> getF()
	{
		return f;
	}
	public static ArrayList<Model> getO()
	{
		return o;
	}
	
	public static void loadFromStream(InputStream in) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		//ObjModel obj = new ObjModel();
		
		boolean pending = false;
		
		while(reader.ready())
		{
			String line = reader.readLine();
			if(line == null) break;
			if(line.equals("")) continue;
			if(line.equals(" ")) continue;
			StringTokenizer st = new StringTokenizer(line);
			String command = st.nextToken();
			
			if(command.equals("#")) continue;
			
			if(command.equals("o"))
			{
				if(pending)
				{
					Model model = new Model();
					model.fill(f, vt.size() > 0, vn.size() > 0);
					o.add(model);
				}
				else
					pending = true;
			}else
                if (command.equals("v")) {
                    v.add(read_point(st));
                }
                else
                if (command.equals("vn")) {
                    vn.add(read_point(st));
                }
                else
                if (command.equals("vt")) {
                    vt.add(read_point(st));
                }
                else
                if (command.equals("f")) {
                    if (st.countTokens() != 3)
                        throw new UnsupportedOperationException("Only triangles supported");

                    Face face = new Face(3);
                    while (st.hasMoreTokens()) {
                        StringTokenizer face_tok = new StringTokenizer(st.nextToken(), "//");

                        int v_idx = -1;
                        int vt_idx = -1;
                        int vn_idx = -1;
                        v_idx = Integer.parseInt(face_tok.nextToken());
                        if (face_tok.hasMoreTokens()) vn_idx = Integer.parseInt(face_tok.nextToken());
                        if (face_tok.hasMoreTokens()) vt_idx = Integer.parseInt(face_tok.nextToken());
                        //Log.v("objmodel", "face: "+v_idx+"/"+vt_idx+"/"+vn_idx);

                        face.addVertex(
                            v.get(v_idx-1),
                            vt_idx == -1 ? null : vt.get(vt_idx-1),
                            vn_idx == -1 ? null : vn.get(vn_idx-1)
                        );
                    }
                    f.add(face);
                }
                /*
                else
                if (cmd.equals("usemtl")) {
                    // lets not bother parsing material file
                    // just use the name as an asset path
                    obj.mTextureName = tok.nextToken();
                }
                */
            }

            if (pending) {
                Model model = new Model();
                model.fill(f, vt.size() > 0, vn.size() > 0);
                o.add(model);
            }

            //obj.mModels = new Model[o.size()];
            //o.toArray(obj.mModels);

           // return obj;
        }
	private static Vertex read_point(StringTokenizer tok)
    {
		Vertex ret = new Vertex();
        if (tok.hasMoreTokens()) {
            ret.x = Float.parseFloat(tok.nextToken());
            if (tok.hasMoreTokens()) {
                ret.y = Float.parseFloat(tok.nextToken());
                if (tok.hasMoreTokens()) {
                    ret.z = Float.parseFloat(tok.nextToken());
                }
            }
        }
        return ret;
    }
}
