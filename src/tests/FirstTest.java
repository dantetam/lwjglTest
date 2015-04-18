package tests;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
 
/**
 * @author Oskar Veerhoek
 */

public class FirstTest {
	
    public static void main(String[] args) {
        try {
            Display.setDisplayMode(new DisplayMode(640, 480));
            Display.setTitle("Episode 1 – Display Test");
            Display.create();
        } catch (LWJGLException e) {
            System.err.println("Display wasn't initialized correctly.");
            System.exit(1);
        }
 
        while (!Display.isCloseRequested()) {
            Display.update();
            Display.sync(60);
        }
 
        Display.destroy();
        System.exit(0);
    }
}