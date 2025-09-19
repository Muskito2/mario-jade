package components;

import jade.Component;

/**
 * The font renderer system. Not yet implemented. Extends {@link Jade.Component}.
 * 
 * @author antoi
 * @version  Dev 1.6 Dirty Flags in Rendering
 *
 */
public class FontRenderer extends Component {
	
	@Override
	public void start() {
		if (gameObject.getComponent(SpriteRenderer.class) != null) {
			System.out.println("Found Font Renderer!");
		}
	}
	
	@Override
	public void update(float dt) {
		
	}
}
