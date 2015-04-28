package levels;

import java.util.ArrayList;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

import render.Loader;
import textures.ModelTexture;
import entities.Entity;

public class LevelManager {

	public ArrayList<Entity> entities;
	
	public LevelManager() {
		entities = new ArrayList<Entity>();
		for (int i = 0; i < 100; i++)
		{
			Vector3f pos = new Vector3f((int)(Math.random()*150), (int)(Math.random()*150), (int)(Math.random()*150));
			Vector3f rot = new Vector3f((int)(Math.random()*180), (int)(Math.random()*180), (int)(Math.random()*180));
			Vector3f size = new Vector3f((int)(Math.random()*20 + 20), (int)(Math.random()*20 + 20), (int)(Math.random()*20 + 20));
			entities.add(newBox(pos, rot, new Vector3f(5,10,15), "bluePlasma"));
		}
	}
	
	public Entity newBox(Vector3f position, Vector3f rotation, Vector3f size, String textureName)
	{
		float x = size.x/2, y = size.y/2, z = size.z/2;
		float[] vertices = {			
				-x,y,-z,	
				-x,-y,-z,	
				x,-y,-z,	
				x,y,-z,		
				
				-x,y,z,	
				-x,-y,z,	
				x,-y,z,	
				x,y,z,
				
				x,y,-z,	
				x,-y,-z,	
				x,-y,z,	
				x,y,z,
				
				-x,y,-z,	
				-x,-y,-z,	
				-x,-y,z,	
				-x,y,z,
				
				-x,y,z,
				-x,y,-z,
				x,y,-z,
				x,y,z,
				
				-x,-y,z,
				-x,-y,-z,
				x,-y,-z,
				x,-y,z
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
		};
		
		Loader loader = new Loader();
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		//RawModel model = OBJLoader.loadObjModel("stall", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture(textureName));
		
		//TODO: texture.transparent = true;
		//TODO: texture.fastLighting = true;
		texture.shineDamper = 10;
		texture.reflectiveness = 1;
		
		TexturedModel texturedModel = new TexturedModel(model, texture);
		Entity entity = new Entity(texturedModel,position,rotation.x,rotation.y,rotation.z,1);
		entity.scale = 1;
		
		return entity;
	}

}
