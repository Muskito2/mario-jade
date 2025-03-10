package jade;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

import util.Time;

import org.lwjgl.glfw.GLFWErrorCallback;
//import org.lwjgl.system.*;
//import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
//import static org.lwjgl.system.MemoryStack.*;

public class Window {
	private int width, height;
	private String title;
	private long glfwWindow; // this number is the address where the window is in the memory
	public float r, g, b, a; // PUBLIC TEMPORARY
	//private boolean fadeToBlack = false; // test TO REMOVE
	
	private static Window window = null; // can be a Window, initially null
	
	// Add more components
	private static Scene currentScene;
	
	private Window() { // ensure the Window class is a singleton ; we only want one Window object 
		this.width = 2560;
		this.height = 1400;
		this.title = "MarioWorld";
		r=1;
		b=1;
		g=1;
		a=1;
	}
	
	// Update scene
	public static void changeScene(int newScene) {
		switch (newScene) {
		case 0:
			currentScene = new LevelEditorScene();
			//currentScene.init()
			break;
		case 1:
			currentScene = new LevelScene();
			break;
		default:
			assert false : "Unknown scene '" + newScene + "'";
			break;
		}
	}
	
	public static Window get() {
		if (Window.window == null ) { // Possible to just say window
			Window.window = new Window();
		}
		
		return Window.window;
	}
	
	public void run() {
		System.out.println("Hello LWGJL" + Version.getVersion() + "!");
		
		init();
		
		loop();
		
		// Free the memory
		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public void init() {
		// Setup an error callback
		GLFWErrorCallback.createPrint(System.err).set(); // will print errors using System.err.println...
		
		// Initialize GLFW
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		// Configure GLFW
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		
		
		// Create the window
		glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		// glfwCreateWindow returned the handle (memory address)
		if (glfwWindow == NULL) {
			throw new IllegalStateException("Failed to create the GLFW window");
		}
		
		// Add the input callbacks
		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
		
		// Make the OpenGL context current
		glfwMakeContextCurrent(glfwWindow);
		// Enable v-sync: buffer swapping
		glfwSwapInterval(1);
		
		// Make the window visible
		glfwShowWindow(glfwWindow);
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		Time.getTimeStarted(); // TO REMOVE
		Window.changeScene(0); // To be sure we are in a scene initially
	}
	
	public void loop() {
		float beginTime = Time.getTime(); // Initialize variable for the time that the frame begin
		float endTime;
		float dt = -1.0f;
		
		while (!glfwWindowShouldClose(glfwWindow)) {
			// Poll events
			glfwPollEvents();
			
			glClearColor(r, g, b, a);
			glClear(GL_COLOR_BUFFER_BIT);
			
			if (dt >= 0) {
				currentScene.update(dt); // lag of two frames, ok for now
			}
			
//			if (fadeToBlack) {
//				r = Math.max(r - 0.01f, 0);
//				g = Math.max(r - 0.01f, 0);
//				b = Math.max(r - 0.01f, 0);
//			}
//			
//			if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
//				//System.out.println("space key is pressed");
//				fadeToBlack = true;
//			}
			
			glfwSwapBuffers(glfwWindow);
			
			endTime = Time.getTime();
			dt = endTime - beginTime; // not used
			beginTime = endTime; // record interruption time lost
		}
	}
}