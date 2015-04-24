package render;

import java.util.ArrayList;
import java.util.HashMap;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;

public class MasterRenderer {

	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);
	
	private HashMap<TexturedModel,ArrayList<Entity>> entities = 
			new HashMap<TexturedModel, ArrayList<Entity>>();
	
	public void render(Light light, Camera camera)
	{
		renderer.prepare();
		shader.start();
		shader.loadLight(light);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear();
	}
	
	//Group entities into certain lists of their own kind
	//i.e. a blue box goes into an arraylist of blue boxes
	public void processEntity(Entity entity)
	{
		TexturedModel entityModel = entity.getModel();
		ArrayList<Entity> allOfType = entities.get(entityModel);
		if (allOfType != null)
			allOfType.add(entity);
		else
		{
			ArrayList<Entity> temp = new ArrayList<Entity>();
			temp.add(entity);
			entities.put(entityModel, temp);
		}
	}
	
	public void cleanUp()
	{
		shader.cleanUp();
	}

}
