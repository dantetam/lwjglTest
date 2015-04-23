package shaders;

import org.lwjgl.util.vector.Matrix4f;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	private int locationTransformMatrix;
	
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
	}

	public void loadTransformMatrix(Matrix4f matrix)
	{
		super.loadMatrix(locationTransformMatrix, matrix);
	}
	
}
