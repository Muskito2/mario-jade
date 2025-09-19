package jade;

/**
 * The abstract class for a component which store {@link jade.GameObject}.
 * 
 * @author antoi
 * @version  Dev 1.6 Dirty Flags in Rendering
 *
 */
public abstract class Component {
	
	public GameObject gameObject = null;
	
	public void start() {
		
	}
	
	public abstract void update(float dt);
}
