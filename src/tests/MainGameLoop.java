package tests;

import levels.LevelManager;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import data.Data;
import entities.Camera;
import entities.Entity;
import entities.Light;
import render.*;
import shaders.StaticShader;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import textures.WhiteTerrainTexturePack;

public class MainGameLoop {
	
	public int frameCount = 0;
	
	public static void main(String[] args)
	{
		Data.setup();
		new MainGameLoop();
	}
	
	public MainGameLoop()
	{
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		LevelManager levelManager = new LevelManager();
		
		TerrainTexture backTexture = new TerrainTexture(loader.loadTexture("seaTexture"));
		TerrainTexture t1 = new TerrainTexture(loader.loadTexture("iceTexture"));
		TerrainTexture t2 = new TerrainTexture(loader.loadTexture("taigaTexture"));
		TerrainTexture t3 = new TerrainTexture(loader.loadTexture("desertTexture"));
		TerrainTexture t4 = new TerrainTexture(loader.loadTexture("steppeTexture"));
		TerrainTexture t5 = new TerrainTexture(loader.loadTexture("dryforestTexture"));
		TerrainTexture t6 = new TerrainTexture(loader.loadTexture("forestTexture"));
		TerrainTexture t7 = new TerrainTexture(loader.loadTexture("rainforestTexture"));
		
		WhiteTerrainTexturePack texturePack = new WhiteTerrainTexturePack(
				backTexture,
				t1,
				t2,
				t3,
				t4,
				t5,
				t6,
				t7
				);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("generatedBlendMap"));
		
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
		
		Terrain terrain1 = new Terrain(0,0,loader,texturePack,blendMap,"heightmap");
		//Terrain terrain2 = new Terrain(-1,0,loader,texturePack,blendMap,"heightmap");
		//Terrain terrain3 = new Terrain(0,-1,loader,texturePack,blendMap,"heightmap");
		//Terrain terrain4 = new Terrain(-1,-1,loader,texturePack,blendMap,"heightmap");
		
		Light light = new Light(new Vector3f(800,50,800), new Vector3f(1,1,1));
		Camera camera = new Camera();
		
		//Keep updating the display until the user exits
		MasterRenderer renderer = new MasterRenderer();
		while (!Display.isCloseRequested())
		{
			//entity.rotate(0,0.3F,0);
			camera.move();
			//camera.yaw += 0.1;
			
			renderer.processTerrain(terrain1);
			//renderer.processTerrain(terrain2);
			//renderer.processTerrain(terrain3);
			//renderer.processTerrain(terrain4);
			//renderer.processEntity(levelManager.entities);
			renderer.processGroups(levelManager.groups);
			//levelManager.groups.get(0).move(0,80+(float)(40*Math.sin((float)frameCount/250F)),0);
			/*for (Entity en: levelManager.entities)
			{
				en.rotate(0,1F,0);
			}*/
			
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
