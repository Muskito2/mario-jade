package jade;

//import java.awt.event.KeyEvent;
import static org.lwjgl.opengl.GL33.*; // Could be replaced by individual static import

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import components.SpriteRenderer;
import renderer.Shader;
import renderer.Texture;
import util.Time;

/**
 * The scene for game level editing. Extends {@link jade.Scene}.
 * 
 * @author antoi
 *
 */
public class LevelEditorScene extends Scene {
	// inherit Camera camera
	private float[] vertexArray = {
		// position						// color					// UV Coordinates
		100.5f, -0.5f, 0.0f,			1.0f, 0.0f, 0.0f, 1.0f,		1, 1, // Bottom right 	0
		-0.5f, 100.5f, 0.0f,			0.0f, 1.0f, 0.0f, 1.0f,		0, 0, // Top left		1
		100.5f, 100.5f, 0.0f,			0.0f, 0.0f, 1.0f, 1.0f,		1, 0, // Top right		2
		-0.5f, -0.5f, 0.0f,				1.0f, 1.0f, 0.0f, 1.0f,		0, 1 // Bottom left		3
	};
	
	// Essential: Must be in counter-clockwise order
	private int[] elementArray = {
			/*
			 		x		x
			 		
			 		
			 		
			 		x		x
			 */
			2, 1, 0, // Top right triangle
			0, 1, 3, // Bottom left triangle
	};
	
	private int vaoID, vboID, eboID; // vertex array object, vertex buffer object, element buffer object
	
	private Shader defaultShader;
	private Texture testTexture;
	
	GameObject testObj;
	private boolean firstTime = false;
	
	/* Constructor */
	public LevelEditorScene() {
		System.out.println("Inside level editor scene");
		
	}
	
	@Override
	public void init() {
		System.out.println("Creating 'test object'");
		this.testObj = new GameObject("test object");
		this.testObj.addComponent(new SpriteRenderer());
		this.addGameObjectToScene(this.testObj);
		
		this.camera = new Camera(new Vector2f());
		//Shader testShader = new Shader("assets/shaders/default.glsl");
		defaultShader = new Shader("assets/shaders/default.glsl");
		defaultShader.compile();
		this.testTexture = new Texture("assets/images/Battlefield6.png");
		// END OF COMPILE AND LINK SHADERS
		
		// VAO, VBO, EBO buffer objects, and send to GPU
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		
		// Create a float buffer of vertices
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
		vertexBuffer.put(vertexArray).flip();
		
		// Create VBO upload the vertex buffer
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		
		// Create the indices and upload
		IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
		elementBuffer.put(elementArray).flip();
		
		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
		
		// Add the vertex attributes pointers
		int positionSize = 3;
		int colorSize = 4;
		int uvSize = 2;
		int floatSizeBytes = Float.BYTES;
		int vertexSizeBytes = (positionSize + colorSize + uvSize) * floatSizeBytes;
		glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0); // The offset is 0
		glEnableVertexAttribArray(0); // At 0 we are putting the position...
		
		glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
		glEnableVertexAttribArray(1);
		
		glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * floatSizeBytes);
		glEnableVertexAttribArray(2); // The third attribute, can really be anything we want to be!
	}
	
	@Override
	public void update(float dt) {
		camera.position.x -= dt * 50.0f;
		camera.position.y -= dt * 20.0f; // !!! forgot !!!
		//System.out.println(1.0f / dt + " FPS");
		defaultShader.use();
		
		// Upload texture to shader
		defaultShader.uploadTexture("TEX_SAMPLER", 0);
		glActiveTexture(GL_TEXTURE0);
		testTexture.bind();
		
		
		defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
		defaultShader.uploadMat4f("uView", camera.getViewMatrix());
		defaultShader.uploadFloat("uTime", (float) Time.getTime()); // Cast in float because time is in double
		
		
		// Bind the VAO that we're using
		glBindVertexArray(vaoID);
		
		// Enable the vertex attribute pointers
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
		
		// Unbind everything
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		defaultShader.detach();
		
		if (!firstTime ) {
			System.out.println("Creating gameObject");
			GameObject go = new GameObject("Game Test 2");
			go.addComponent(new SpriteRenderer());
			this.addGameObjectToScene(go);
			firstTime = true;
		}
		
		
		for (GameObject go : this.gameObjects) {
			go.update(dt);
		}
	}
}