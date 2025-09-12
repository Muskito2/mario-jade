package jade;

import org.joml.Vector2f;
import org.joml.Vector4f;

import components.SpriteRenderer;
import util.AssetPool;

/**
 * The scene for game level editing. Extends {@link jade.Scene}.
 * 
 * @author antoi
 * @version 2.0 Major changes in Dev 1.4.0
 *
 */
public class LevelEditorScene extends Scene {
	
	/* Constructor */
	public LevelEditorScene() {
		System.out.println("");
	}
	
	@Override
	public void init() {
		this.camera = new Camera(new Vector2f());
		
		GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100,100), new Vector2f(256,256)));
		obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/Battlefield6.png")));
		this.addGameObjectToScene(obj1);
		
		GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400,100), new Vector2f(256,256)));
		obj2.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/Battlefield6.png")));
		this.addGameObjectToScene(obj2);
		
		
		loadRessources();
	}
	
	private void loadRessources() {
		AssetPool.getShader("assets/shaders/default.glsl");
	}
	
	@Override
	public void update(float dt) {
		System.out.println(1.0f / dt + " FPS");

		for (GameObject go : this.gameObjects) {
			go.update(dt);
		}
		
		this.renderer.render();
	}
}