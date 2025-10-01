package jade;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import util.Time;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;


/**
 * 
 * The window containing all the graphics elements of the application.
 *
 * @version Dev 1.7 Blending
 */
@SuppressWarnings("unused")
public class Window {
	
	/**
	 * The dimensions of the window.
	 */
	private int width, height;
	/**
	 * The title of the window.
	 */
	private String title;
	/**
	 * The address where the window is in the memory.
	 */
	private long glfwWindow;
	/**
	 * A color value in the RGBA system.
	 */
	
	private ImGuiLayer imGuiLayer;
	public float r, g, b, a; // PUBLIC TEMPORARY
	/**
	 * The single window belonging making up the class.
	 */
	private static Window window = null;
	/**
	 * The Scene currently displayed in the window.
	 */
	private static Scene currentScene;
	
	/**
	 * The constructor of the class Window.
	 * 
	 * There is no parameters and every attribute are given arbitrary value from the constructor.
	 */
	private Window() { // ensure the Window class is a singleton ; we only want one Window object 
		
		this.width = 1920;
		this.height = 1080;
		this.title = "MarioWorld";
		this.r=1;
		this.b=1;
		this.g=1;
		this.a=1;
	}
	
	/**
	 * 
	 * @return The singleton window.
	 */
	public static Window get() {
		if (Window.window == null ) {
			Window.window = new Window();
		}
		
		return Window.window;
	}
	
	public static Scene getScene() {
		return get().currentScene;
	}
	
	/**
	 * 
	 * Update the scene.
	 * 
	 * @param newScene The reference number of the new scene to display.
	 */
	public static void changeScene(int newScene) {
		switch (newScene) {
		case 0:
			currentScene = new LevelEditorScene();
			currentScene.init();
			currentScene.start();
			break;
		case 1:
			currentScene = new LevelScene();
			currentScene.init();
			currentScene.start();
			break;
		default:
			assert false : "Unknown scene '" + newScene + "'";
			break;
		}
	}
	
	/**
	 * Initiate the window code of the application. It calls {@link #init()} (for system window and OpenGL context) and {@link #loop()} (for the game loop)
	 */
	public void run() {
		
		init();
		
		loop(); // Execute on one thread only, code after only execute when the looping stops.
		
		// Free the memory
		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	/**
	 * Initialize the window with LWJGL which contains the GLFW library. This make possible to create a window, an open GL context and manage input.
	 * 
	 * This code is mostly taken from the GLFW tutorial on the LWJGL website.
	 */
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
		
		
		// Create the window and return the handle (memory address)
		glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if (glfwWindow == NULL) {
			throw new IllegalStateException("Failed to create the GLFW window");
		}
		
		// Add the input callbacks // LISTENER
		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
		
		glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
			Window.setWidth(newWidth);
			Window.setHeight(newHeight);
		});
		
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
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		
		this.imGuiLayer = new ImGuiLayer(glfwWindow);
		this.imGuiLayer.initImGui();
		
		Window.changeScene(0); // Showing LevelEditorScene
	}
	
	/**
	 * Enable a time based application by looping. The goal is to have the game time to be independent from the time it takes for the frame to render.
	 */
	public void loop() {
		
		double beginTime = Time.getTime(); // Initialize variable for the time that the frame begin
		double endTime;
		float dt = -1;
		
		while (!glfwWindowShouldClose(glfwWindow)) {
			// Polls events, calls the inputs callbacks.
			glfwPollEvents();
			
			glClearColor(r, g, b, a);
			glClear(GL_COLOR_BUFFER_BIT);
			
			if (dt >= 0) {
				currentScene.update(dt); // Lag of two frames, ok for now
			}
			
			this.imGuiLayer.update(dt);
			glfwSwapBuffers(glfwWindow);
			
			endTime = Time.getTime();
			dt = (float)(endTime - beginTime);
			beginTime = endTime;
		}
	}
	
	public static int getWidth() {
		return get().width;
	}
	
	public static int getHeight() {
		return get().height;
	}
	
	public static void setWidth(int newWidth) {
		get().width = newWidth;
		System.out.println("window size callback width changed");
	}
	
	public static void setHeight(int newHeight) {
		get().height = newHeight;
	}
}