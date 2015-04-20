package render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Loader {

	//Store VAOs and VBOs indices as reference for future clean up
	private ArrayList<Integer> vaos = new ArrayList<Integer>();
	private ArrayList<Integer> vbos = new ArrayList<Integer>();
	
	//Create a new model from float data, which accessed by the renderer
	public RawModel loadToVAO(float[] pos, int[] indices)
	{
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeData(0, pos);
		unbindVAO();
		return new RawModel(vaoID,indices.length);
	}
	
	//Delete VAOs and VBOs by finding their vertices
	public void cleanData()
	{
		for (int i: vaos)
			GL30.glDeleteVertexArrays(i);
		for (int i: vbos)
			GL15.glDeleteBuffers(i);
	}
	
	//Request a new VAO id, store that ID, and bind it
	private int createVAO()
	{
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeData(int attribNum, float[] data)
	{
		int vboID = GL15.glGenBuffers(); //Request a VBO id
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); //Bind it for use
		FloatBuffer buffer = toFloatBuffer(data); //Convert the data to a float buffer
		
		//Store the buffer in the VBO, for use in a static draw (vs. a dynamic draw)
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
		//Indicate that triangles are being drawn
		GL20.glVertexAttribPointer(attribNum,3,GL11.GL_FLOAT,false,0,0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //Unbind the current VBO being used
	}
	
	//Unbind the current VAO being used
	private void unbindVAO()
	{
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = toIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer toIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	//Convert an array of floats to a float buffer object
	private FloatBuffer toFloatBuffer(float[] data)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}
