package jade;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {
	private boolean changingScene = false;
	private float timeToChangeScene = 2.0f;
	
	public LevelEditorScene() {
		System.out.println("Inside level editor scene");
		
	}
	
	@Override
	// Use this to transition to another scene
	public void update(float dt) {
		
		//System.out.println("" + (1.0f / dt) + "FPS");
		
		if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
			changingScene = true;
		}
		
		if (changingScene && timeToChangeScene > 0) {
			timeToChangeScene -= dt;
			Window.get().r -= dt * 5.0f;
			Window.get().g -= dt * 5.0f;
			Window.get().b -= dt * 5.0f;
		} else if (changingScene) {
			Window.changeScene(1);
		}
	}

}
