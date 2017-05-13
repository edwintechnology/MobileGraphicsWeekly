package com.example.opengles20_projection;


import java.util.ArrayList;

public class Vertices {

	private ArrayList<Vertex> Vertices;
	private int vert;

	public Vertices() {
		Vertices = new ArrayList<Vertex>();
		vert = 0;
	}

	public Vertices(Vertex[] vertices) {
		Vertices = new ArrayList<Vertex>();
		vert = 0;
		for (Vertex v : vertices) {
			Vertices.add(v);
			vert++;
		}
	}

	public void add(Vertex v) {
		Vertices.add(v);
		vert++;
	}

	public void remove(Vertex v) {
		Vertices.remove(v);
		vert--;
	}

	public Vertex get(int index) {
		return Vertices.get(index);
	}
	
	public int size() {
		return vert;
	}

	public ArrayList<Vertex> getVertice() {
		return Vertices;
	}

	public void Print() {
		for (Vertex v : Vertices) {
			System.out.printf("(" + v.x + ", " + v.y + ", " + v.z + ")");
		}
	}
	public float[] ToArray()
	{
		float[] line = new float[vert * 3];
		int line_count = 0;
		for(Vertex v : Vertices)
		{
			line[line_count++] = v.x;
			line[line_count++] = v.y;
			line[line_count++] = v.z;
		}
		
		return line;
	}
}