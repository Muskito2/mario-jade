package jade;

import org.joml.Vector2f;
import org.joml.Vector4f;

import components.SpriteRenderer;

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
		
		int xOffset = 10;
		int yOffset = 10;
		
		float totalWidth = (float)(600 - xOffset * 2);
		float totalHeight = (float)(300 - yOffset * 2);
		float sizeX = totalWidth / 100.0f;
		float sizeY = totalHeight / 100.0f;
		
		for (int x=0; x < 100; x++) {
			for (int y=0; y < 100; y++) {
				float xPos = xOffset + (x * sizeX);
				float yPos = yOffset + (y * sizeY);
				
				GameObject go = new GameObject("Obj" + x + "" + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
				go.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
				this.addGameObjectToScene(go);
			}
		}
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