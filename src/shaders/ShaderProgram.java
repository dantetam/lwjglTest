package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class ShaderProgram {

	public int programID, vertexShaderID, fragmentShaderID;
	
	public ShaderProgram(String vertex, String fragment)
	{
		vertexShaderID = loadShader(vertex,GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragment,GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		bindAttributes();
	}
	
	public void start()
	{
		GL20.glUseProgram(programID);
	}
	
	public void stop()
	{
		GL20.glUseProgram(0);
	}
	
	public void cleanUp()
	{
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String varName)
	{
		GL20.glBindAttribLocation(programID, attribute, varName);
	}
	
	private static int loadShader(String file, int type)
	{
		StringBuilder shaderSource = new StringBuilder();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null)
			{
				shaderSource.append(line).append("\n");
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShader(shaderID,GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
		{
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader");
			System.exit(-1);
		}
		return shaderID;
	}

}
