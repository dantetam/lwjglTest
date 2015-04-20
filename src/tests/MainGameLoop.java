package tests;

import org.lwjgl.opengl.Display;

import render.*;

public class MainGameLoop {
	
	public static void main(String[] args)
	{
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		//counter clockwise vertices
		float[] vertices = {
				//Left bottom and top right, resp.
			-0.5f, 0.5f, 0f,	
			-0.5f, -0.5f, 0f,
			0.5f, -0.5f, 0f,
			0.5f, 0.5f, 0f
		};
		
		int[] indices = {0,1,3,3,1,2};
		
		RawModel model = loader.loadToVAO(vertices, indices);
		
		//Keep updating the display until the user exits
		while (!Display.isCloseRequested())
		{
			renderer.prepare();
			renderer.render(model);
			DisplayManager.updateDisplay();
		}
		
		//Clean up data
		loader.cleanData();
		DisplayManager.closeDisplay();
	}

}
