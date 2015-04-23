package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import toolbox.Maths;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	private int locationTransformMatrix, locationProjectionMatrix, locationViewMatrix;
	
	public StaticShader() 
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	protected void bindAttributes() 
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	protected void getAllUniformLocations() 
	{
		//Find the location of the transformMatrix uniform variable
		locationTransformMatrix = super.getUniformLocation("transformMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
	}

	public void loadTransformMatrix(Matrix4f matrix) {super.loadMatrix(locationTransformMatrix, matrix);}
	public void loadProjectionMatrix(Matrix4f matrix) {super.loadMatrix(locationProjectionMatrix, matrix);}
	
	//Create a new view matrix based on the properties of the camera
	public void loadViewMatrix(Camera camera) 
	{
		Matrix4f matrix = Maths.createViewMatrix(camera);
		super.loadMatrix(locationViewMatrix, matrix);
	}
	
}
