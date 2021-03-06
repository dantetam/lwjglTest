package render;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import models.RawModel;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import data.Data;

public class Loader {

	//Store VAOs and VBOs indices as reference for future clean up
	private ArrayList<Integer> vaos = new ArrayList<Integer>();
	private ArrayList<Integer> vbos = new ArrayList<Integer>();
	private ArrayList<Integer> textures = new ArrayList<Integer>();
	
	//Create a new model from float data, which accessed by the renderer
	public RawModel loadToVAO(float[] pos, float[] textureCoords, float[] normals, int[] indices)
	{
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeData(0, 3, pos); //Store the position (3-tuples) in pos 0 of the VAO
		storeData(1, 2, textureCoords);
		storeData(2, 3, normals);
		unbindVAO();
		//There are repeats in the old pos[] so indices now contains the correct number of indices
		return new RawModel(vaoID,indices.length);
	}
	
	//Generate new data with no normals
	/*public RawModel loadToVAO(float[] pos, float[] textureCoords, int[] indices)
	{
		float[] normals = new float[indices.length];
		for (int i = 0; i < indices.length; i += 3)
		{
			//I guess the implementers of OpenGL disliked decent OOP design
			Vector3f p0 = new Vector3f(pos[indices[i]*3], pos[indices[i]*3 + 1], pos[indices[i]*3 + 2]),
					p1 = new Vector3f(pos[indices[i+1]*3], pos[indices[i+1]*3 + 1], pos[indices[i+1]*3 + 2]),
					p2 = new Vector3f(pos[indices[i+2]*3], pos[indices[i+2]*3 + 1], pos[indices[i+2]*3 + 2]);
			Vector3f cross1 = null, cross2 = null;
			cross1 = Vector3f.sub(p1, p0, cross1); //Why is there no "p1.sub(p0)" notation?
			cross2 = Vector3f.sub(p2, p0, cross2);
			Vector3f normal = null;
			normal = Vector3f.cross(cross1, cross2, normal);
			normals[i] = -normal.x; normals[i+1] = -normal.y; normals[i+2] = -normal.z;
			Vector3f u = null, v = null;
			u = Vector3f.sub(p1, p0, u); 
			v = Vector3f.sub(p2, p0, v);
			Vector3f notUnit = new Vector3f();
			notUnit.x = u.z*v.y - u.y*v.z;
			notUnit.y = u.x*v.z - u.z*v.x;
			notUnit.z = u.y*v.x - u.x*v.y;
			Vector3f normal = (Vector3f)notUnit.normalise();
			normals[i] = normal.x; normals[i+1] = normal.y; normals[i+2] = normal.z;
		}
		return loadToVAO(pos, textureCoords, normals, indices);
	}*/
	
	public int textureAcc = 300;
	public int loadTexture(String fileName)
	{
		Texture texture = null;
		try 
		{
			texture = TextureLoader.getTexture("PNG",new FileInputStream("res/"+fileName+".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
			//System.out.println("Successfully loaded texture " + "res/"+fileName+".png");
		} 
		catch (Exception e) 
		{
			Data.createTexture(Integer.parseInt(fileName.substring(12)));
			try {texture = TextureLoader.getTexture("PNG",new FileInputStream("res/"+fileName+".png"));} catch (IOException e1) {e1.printStackTrace();}
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		}
		int textureID = textureAcc;
		textureAcc++;
		textures.add(textureID);
		return textureID;
	}
	
	//Delete VAOs and VBOs by finding their vertices
	public void cleanData()
	{
		for (int i: vaos)
			GL30.glDeleteVertexArrays(i);
		for (int i: vbos)
			GL15.glDeleteBuffers(i);
		for (int i: textures)
			GL11.glDeleteTextures(i);
	}
	
	//Request a new VAO id, store that ID, and bind it
	private int createVAO()
	{
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeData(int attribNum, int coordinateSize, float[] data)
	{
		int vboID = GL15.glGenBuffers(); //Request a VBO id
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); //Bind it for use
		FloatBuffer buffer = toFloatBuffer(data); //Convert the data to a float buffer
		
		//Store the buffer in the VBO, for use in a static draw (vs. a dynamic draw)
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
		//Indicate that triangles are being drawn
		GL20.glVertexAttribPointer(attribNum,coordinateSize,GL11.GL_FLOAT,false,0,0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //Unbind the current VBO being used
	}
	
	//Unbind the current VAO being used
	private void unbindVAO()
	{
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices)
	{
		int vboID = GL15.glGenBuffers(); //Request a VBO id
		vbos.add(vboID); 
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID); //Note "ELEMENT_ARRAY" not "ARRAY"
		IntBuffer buffer = toIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); //Store the data in the bound VBO
	}
	
	//Convert arrays of numbers to the respective buffers
	private IntBuffer toIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	private FloatBuffer toFloatBuffer(float[] data)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}
