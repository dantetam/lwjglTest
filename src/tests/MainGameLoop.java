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
			
			0.5f, -0.5f, 0f,
			0.5f, 0.5f, 0f,	
			-0.5f, 0.5f, 0f	
		};
		
		RawModel model = loader.loadToVAO(vertices);
		
		while (!Display.isCloseRequested())
		{
			renderer.prepare();
			renderer.render(model);
			DisplayManager.updateDisplay();
		}
		
		loader.cleanData();
		DisplayManager.closeDisplay();
	}

}
