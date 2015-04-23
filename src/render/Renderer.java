package render;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;
import shaders.StaticShader;
import toolbox.Maths;
import entities.Entity;

public class Renderer {

	private static final float FOV = 70, NEAR_PLANE = 0.1f, FAR_PLANE = 1000f;
	
	private Matrix4f projectionMatrix;
	
	public Renderer(StaticShader shader)
	{
		//Create a new matrix, for the first time
		createProjectionMatrix();
		//Access the shader to load the projectionMatrix
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void prepare()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(1,0,0,0);
	}
	
	//The textured model encapsulates a RawModel
	//Access a VAO by using the data contained within a RawModel object
	public void render(Entity entity, StaticShader shader)
	{
		TexturedModel texturedModel = entity.getModel();
		RawModel model = texturedModel.getRawModel();
		
		//Whenever a VAO is edited, it must be bound
		GL30.glBindVertexArray(model.vaoID);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		//Access transformMatrix
		Matrix4f transformMatrix = Maths.createTransformMatrix(
				entity.position, 
				entity.rotX, 
				entity.rotY, 
				entity.rotZ, 
				entity.scale
				);
		shader.loadTransformMatrix(transformMatrix);
		
		//Texture bank 0
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().textureID);
		
		//0 indicates the first vertex to be drawn in triangles
		//vertexCount property indicates upper bound
		
		//GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.vertexCount); 
		//replace the above method because we are using
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.vertexCount, GL11.GL_UNSIGNED_INT, 0);
		
		//Disable after finished rendering
		GL20.glDisableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		GL30.glBindVertexArray(0); //Unbind the current bound VAO
	}
	
	private void createProjectionMatrix()
	{
		float ar = (float)Display.getWidth()/(float)Display.getHeight();
		float yScale = (float)(1f/Math.tan(Math.toRadians(FOV/2f)))*ar;
		float xScale = yScale/ar;
		float frustumLength = FAR_PLANE - NEAR_PLANE;
		
		//Set up the projection matrix by declaring discrete values
		//These values are calculated by matrix math
		projectionMatrix = new Matrix4f(); //Initialized to zeroes, not identity
		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = (FAR_PLANE + NEAR_PLANE)/-frustumLength;
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -(2*FAR_PLANE*NEAR_PLANE / frustumLength);
		projectionMatrix.m33 = 0;
	}

}
