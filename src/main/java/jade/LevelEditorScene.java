package jade;

//import java.awt.event.KeyEvent;
import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import kotlin.jvm.internal.MagicApiIntrinsics;

public class LevelEditorScene extends Scene {
	private String vertexShaderSrc = "#version 330 core\r\n" + 
			"\r\n" + 
			"layout (location=0) in vec3 aPos;\r\n" + 
			"layout (location=1) in vec4 aColor;\r\n" + 
			"\r\n" + 
			"out vec4 fColor;\r\n" + 
			"\r\n" + 
			"void main()\r\n" + 
			"{\r\n" + 
			"    fColor = aColor;\r\n" + 
			"    gl_Position = vec4(aPos, 1.0);	\r\n" + 
			"}";
	private String fragmentShaderSrc = "#version 330 core\r\n" + 
			"\r\n" + 
			"in vec4 fColor;\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"out vec4 color;\r\n" + 
			"\r\n" + 
			"void main()\r\n" + 
			"{\r\n" + 
			"    color = fColor;\r\n" + 
			"}";
	private int vertexID, fragmentID, shaderProgram;
	
	private float[] vertexArray = {
		// position					// color
		0.5f, -0.5f, 0.0f,			1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
		-0.5f, 0.5f, 0.0f,			0.0f, 1.0f, 0.0f, 1.0f, // Top left		1
		0.5f, 0.5f, 0.0f,			0.0f, 0.0f, 1.0f, 1.0f, // Top right	2
		-0.5f, -0.5f, 0.0f,			1.0f, 1.0f, 0.0f, 1.0f, // Bottom left	3
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
	
	private int vaoID, vboID, eboID; // vertex array object, buffer object, element buffer object
	
	public LevelEditorScene() {
		System.out.println("Inside level editor scene");
		
	}
	
	@Override
	public void init() {
		// First load and compile the vertex shader
		vertexID = glCreateShader(GL_VERTEX_SHADER); // Import only what's needed?
		// Pass the shader source to the GPU
		glShaderSource(vertexID, vertexShaderSrc);
		glCompileShader(vertexID);
		
		// Check for errors in compilation
		int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed.");
			System.out.println(glGetShaderInfoLog(vertexID, len));
			//assert false : ""; // Not working for now
		}
		
		// First load and compile the fragment shader
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER); // Import only what's needed?
		// Pass the shader source to the GPU
		glShaderSource(fragmentID, fragmentShaderSrc);
		glCompileShader(fragmentID);
		
		// Check for errors in compilation
		success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: 'defaultShader.glsl'\n\tFragment shader compilation failed.");
			System.out.println(glGetShaderInfoLog(vertexID, len));
			//assert false : "";
		}
		
		
		// Link shader and check for errors
		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexID);
		glAttachShader(shaderProgram, fragmentID);
		glLinkProgram(shaderProgram);
		
		success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
		if (success == GL_FALSE) {
			int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: 'defaultShader.glsl'\n\tLinking shaders failed.");
			System.out.println(glGetProgramInfoLog(shaderProgram, len));
			//assert false : "";
		}
		
		
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
		int floatSizeBytes = 4; // magic number or not... but we know a float is 4 bytes
		int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
		glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0); // the offset is 0
		glEnableVertexAttribArray(0); // at 0 we are putting the position
		
		glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
		glEnableVertexAttribArray(1);
	}
	
	@Override
	// Use this to transition to another scene
	public void update(float dt) {
		//System.out.println("" + (1.0f / dt) + "FPS");
		// Bind shader program
		glUseProgram(shaderProgram);
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
		
		glUseProgram(0);
		
		
	}

}
