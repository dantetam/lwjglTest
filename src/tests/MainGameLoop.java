package tests;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import render.*;
import shaders.StaticShader;
import terrain.Terrain;
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
		
		/*//counter clockwise vertices
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
		float[] textureCoords = {0,0,0,1,1,1,1,0};*/
		
		/*float[] vertices = {			
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,0.5f,-0.5f,		
				
				-0.5f,0.5f,0.5f,	
				-0.5f,-0.5f,0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				0.5f,0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				-0.5f,-0.5f,0.5f,	
				-0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f
		};
		
		float[] textureCoords = {
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0
		};
		
		int[] indices = {
				0,1,3,	
				3,1,2,	
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,	
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22
		};*/
		
		//RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		RawModel model = OBJLoader.loadObjModel("stall", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
		texture.shineDamper = 10;
		texture.reflectiveness = 1;
		
		TexturedModel texturedModel = new TexturedModel(model, texture);
		Entity entity = new Entity(texturedModel,new Vector3f(0,0,-20),0,0,0,1);
		
		Terrain terrain1 = new Terrain(-1,0,loader,new ModelTexture(loader.loadTexture("bluePlasma")));
		Terrain terrain2 = new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("bluePlasma")));
		
		Light light = new Light(new Vector3f(0,0,-5), new Vector3f(1,1,1));
		Camera camera = new Camera();
		
		//Keep updating the display until the user exits
		MasterRenderer renderer = new MasterRenderer();
		while (!Display.isCloseRequested())
		{
			entity.rotate(0,0.3F,0);
			camera.move();
			//camera.yaw += 0.1;
			
			renderer.processTerrain(terrain1);
			renderer.processTerrain(terrain2);
			renderer.processEntity(entity);
			
			renderer.render(light, camera);
			
			DisplayManager.updateDisplay();
			frameCount++;
		}

		//Do some clean up of all data
		renderer.cleanUp();
		loader.cleanData();
		DisplayManager.closeDisplay();
	}

}
