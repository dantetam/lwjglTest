package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

//There is no real camera in OpenGL
//Every object in the world must be moved in the opposite direction of the camera's movement

public class Camera {

	public Vector3f position = new Vector3f(500,10,500);
	public float pitch, yaw, roll; //High-low, left-right, tilt

	public Camera() {}
	//public Camera(Vector3f p, float a, float b, float c) {}

	public void move()
	{
		float step = 1f;
		if (Keyboard.isKeyDown(Keyboard.KEY_I))
			position.y -= step;
		if (Keyboard.isKeyDown(Keyboard.KEY_O))
			position.y += step;
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			position.x -= step*Math.cos(Math.toRadians(yaw));
			position.z -= step*Math.sin(Math.toRadians(yaw));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			position.x += step*Math.cos(Math.toRadians(yaw));
			position.z += step*Math.sin(Math.toRadians(yaw));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			//laziness, oops
			position.x -= step*Math.cos(Math.toRadians(yaw-90));
			position.z -= step*Math.sin(Math.toRadians(yaw-90));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			//+90 is clockwise, this is a right turn from pointing left (the 'a' command)
			position.x -= step*Math.cos(Math.toRadians(yaw+90));
			position.z -= step*Math.sin(Math.toRadians(yaw+90));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q))
			yaw -= 0.2;
		if (Keyboard.isKeyDown(Keyboard.KEY_E))
			yaw += 0.2;
	}

}
