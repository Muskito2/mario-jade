package jade;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for a scene displayed on screen. A scene manage the time elapsed for what happens inside the scene via the {@link update()} method.
 * 
 * @author antoi
 *
 */
public abstract class Scene {
	
	protected Camera camera;
	private boolean isRunning = false;
	protected List<GameObject> gameObjects = new ArrayList<>();
	
	public Scene() {
		
	}
	
	public void init() {
		
	}
	
	public void start() {
		for (GameObject go : gameObjects) {
			go.start();
		}
		
		isRunning = true;
	}
	
	public void addGameObjectToScene(GameObject go) {
		if (!isRunning) {
			gameObjects.add(go);
		} else {
			gameObjects.add(go);
			go.start();
		}
	}
	
	public abstract void update(float dt);
}
