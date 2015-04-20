package render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {

	public void prepare()
	{
		GL11.glClearColor(1,0,0,0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	//Access a VAO by using the data contained within a RawModel object
	public void render(RawModel model)
	{
		//Whenever a VAO is edited, it must be bound
		GL30.glBindVertexArray(model.vaoID);
		GL20.glEnableVertexAttribArray(0);
		//0 indicates the first vertex to be drawn in triangles
		//vertexCount property indicates upper bound
		//GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.vertexCount);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.vertexCount, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0); //Unbind the current bound VAO
	}

}
