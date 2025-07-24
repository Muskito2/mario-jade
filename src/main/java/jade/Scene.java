package jade;

/**
 * Abstract class for a scene displayed on screen. A scene manage the time elapsed for what happens inside the scene via the {@link update()} method.
 * 
 * @author antoi
 *
 */
public abstract class Scene { // Going to implements some methods
	
	protected Camera camera;
	
	public Scene() {
		
	}
	
	public void init() {
		
	}
	
	public abstract void update(float dt);
}
