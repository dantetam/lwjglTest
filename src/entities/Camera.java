package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

//There is no real camera in OpenGL
//Every object in the world must be moved in the opposite direction of the camera's movement

public class Camera {

	public Vector3f position = new Vector3f(0,0,0);
	public float pitch, yaw, roll; //High-low, left-right, tilt
	
	public Camera() {}
	//public Camera(Vector3f p, float a, float b, float c) {}
	
	public void move()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			position.y += 0.02f;
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			position.y -= 0.02f;
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			position.x -= 0.02f;
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			position.x += 0.02f;
		if (Keyboard.isKeyDown(Keyboard.KEY_Q))
			position.z += 0.02f;
		if (Keyboard.isKeyDown(Keyboard.KEY_E))
			position.z -= 0.02f;
	}
	
}
