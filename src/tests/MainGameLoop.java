package tests;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import render.*;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {
	
	public int frameCount = 0;
	
	public static void main(String[] args)
	{
		new MainGameLoop();
	}
	
	public MainGameLoop()
	{
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		//counter clockwise vertices
		float[] vertices = {
				//Left bottom and top right, resp.
			-0.5f, 0.5f, 0f,	
			-0.5f, -0.5f, 0f,
			0.5f, -0.5f, 0f,
			0.5f, 0.5f, 0f
		};
		
		//order in which to transverse the vertices
		int[] indices = {0,1,3,3,1,2};
		
		//respective u,v vertex of texture to map to
		float[] textureCoords = {0,0,0,1,1,1,1,0};
		
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("blueplasma"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		Entity entity = new Entity(texturedModel,new Vector3f(0,0,-1),0,0,0,1);
		
		Camera camera = new Camera();
		
		//Keep updating the display until the user exits
		while (!Display.isCloseRequested())
		{
			renderer.prepare();
			camera.move();
			shader.start(); //Enable shader
			shader.loadViewMatrix(camera);
			renderer.render(entity,shader);
			shader.stop(); //Disable shader when the draw is done
			DisplayManager.updateDisplay();
			frameCount++;
		}
		
		//Clean up data
		shader.cleanUp();
		loader.cleanData();
		DisplayManager.closeDisplay();
	}

}
