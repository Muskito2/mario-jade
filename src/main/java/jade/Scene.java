package jade;

public abstract class Scene { // Going to implements some methods
	
	protected Camera camera;
	
	public Scene() {
		
	}
	
	public void init() {
		
	}
	
	public abstract void update(float dt);
}
