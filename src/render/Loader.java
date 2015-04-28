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
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

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
	public RawModel loadToVAO(float[] pos, float[] textureCoords, int[] indices)
	{
		float[] normals = new float[indices.length/3];
		/*for (int i = 0; i < normals.length; i++)
			if (i % 3 == 1)
				normals[i] = 1;*/
		return loadToVAO(pos, textureCoords, normals, indices);
	}
	
	public int loadTexture(String fileName)
	{
		Texture texture = null;
		try 
		{
			texture = TextureLoader.getTexture("PNG",new FileInputStream("res/"+fileName+".png"));
		} 
		catch (Exception e) {e.printStackTrace();}
		int textureID = texture.getTextureID();
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
