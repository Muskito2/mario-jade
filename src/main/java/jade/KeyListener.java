package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * 
 * The class to manage mouse events.
 * 
 * The class declare the callbacks GLFW will call when a key (from keyboard or controller) is pressed or released.
 * The usage can be found in the init mehtod of the window, {@link jade.Window#init()}
 * 
 * @author Antoine
 * 
 *
 */
public class KeyListener {
	private static KeyListener instance;
	private boolean keyPressed[] = new boolean[350]; // about 350 bindings in GFLW
	
	private KeyListener() {
		
	}
	
	public static KeyListener get() {
		if (KeyListener.instance == null) {
			KeyListener.instance = new KeyListener();
		}
		
		return KeyListener.instance;
	}
	
	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_PRESS) {
			get().keyPressed[key] = true;
		} else if (action == GLFW_RELEASE) {
			get().keyPressed[key] = false; 
		}
	}
	
	public static boolean isKeyPressed(int keyCode) {
		return get().keyPressed[keyCode];
	}
}
