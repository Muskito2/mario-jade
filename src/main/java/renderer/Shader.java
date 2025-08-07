package renderer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

/**
 * Abstract the use of shaders with GLSL file loading, parsing, compiling, linking and GPU loading. GLSL documentation can be viewed on docs.gl and khronos.org.
 * 
 * @author antoi
 *
 */
public class Shader {
	
	private int shaderProgramID;
	private boolean beingUsed = false;
	
	private String vertexSource;
	private String fragmentSource;
	private String filepath;
	
	public Shader(String filepath) {
		this.filepath = filepath;
		
		// Always do try-catch when opening a file
		try {
			String source = new String(Files.readAllBytes(Paths.get(this.filepath))); // Opening and loading the file
			String[] splitString = source.split("(#type)( )+([a-zA-Z]+)"); // ( )+ match any spaces
			// Note: Array = iterator, testing it:
//			for (String theString : splitString) {
//				System.out.println(theString);
//			}
			// splitString[0] is empty because it captures the empty string before the first match
			
			
			// Find the first pattern (GLSL type)
			int index = source.indexOf("#type") + 6; // In our file, the first #type is at index 0
			int eol = source.indexOf("\r\n", index); // Finding the end of the line
			String firstPattern = source.substring(index, eol).trim(); // vertex, verify if it is this or anything else
			
			// Find the second pattern (GLSL type)
			index = source.indexOf("#type", eol) + 6;
			eol = source.indexOf("\r\n", index);
			String secondPattern = source.substring(index, eol).trim();
			
			if (firstPattern.equals("vertex")) {
				vertexSource = splitString[1];
			} else if (firstPattern.equals("fragment")) {
				fragmentSource = splitString[1];
			} else throw new IOException("Not valid token '" + firstPattern + "'");
			
			if (secondPattern.equals("vertex")) {
				vertexSource = splitString[2];
			} else if (secondPattern.equals("fragment")) {
				fragmentSource = splitString[2];
			} else throw new IOException("Not valid token '" + secondPattern + "'");
			
		} catch(IOException e) {
			e.printStackTrace();
			assert false : "Error: Could not open file for shader: '" + this.filepath + "'";
		}
		
//		System.out.println(vertexSource);
//		System.out.println(fragmentSource);
		
	}
	
	public void compile() {
		int vertexID, fragmentID;
		//int vaoID, vboID, eboID; // vertex array object, buffer object, element buffer object
		// First load and compile the vertex shader
				vertexID = glCreateShader(GL_VERTEX_SHADER); // Import only what's needed?
				// Pass the shader source to the GPU
				glShaderSource(vertexID, vertexSource);
				glCompileShader(vertexID);
				
				// Check for errors in compilation
				int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
				if (success == GL_FALSE) {
					int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
					System.out.println("ERROR: '" + this.filepath + "'\n\tVertex shader compilation failed.");
					System.out.println(glGetShaderInfoLog(vertexID, len));
					assert false : "";
				}
				
				// First load and compile the fragment shader
				fragmentID = glCreateShader(GL_FRAGMENT_SHADER); // Import only what's needed?
				// Pass the shader source to the GPU
				glShaderSource(fragmentID, fragmentSource);
				glCompileShader(fragmentID);
				
				// Check for errors in compilation
				success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
				if (success == GL_FALSE) {
					int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
					System.out.println("ERROR: '" + this.filepath + "'\n\tFragment shader compilation failed.");
					System.out.println(glGetShaderInfoLog(vertexID, len));
					assert false : "";
				}
				
				
				// Link shader and check for errors
				shaderProgramID = glCreateProgram();
				glAttachShader(shaderProgramID, vertexID);
				glAttachShader(shaderProgramID, fragmentID);
				glLinkProgram(shaderProgramID);
				
				success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
				if (success == GL_FALSE) {
					int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
					System.out.println("ERROR: '" + this.filepath + "'\n\tLinking shaders failed.");
					System.out.println(glGetProgramInfoLog(shaderProgramID, len));
					//assert false : "";
				}
	}
	
	/**
	 * Bind shader program
	 */
	public void use() {
		if (!beingUsed) {
			glUseProgram(shaderProgramID);
			beingUsed = true;
		}
		
	}
	
	public void detach() {
		glUseProgram(0);
		beingUsed = false;
	}
	
	public void uploadMat4f(String varName, Matrix4f mat4) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer); // Upload a buffer to GL
		glUniformMatrix4fv(varLocation, false, matBuffer);
	}
	
	public void uploadMat3f(String varName, Matrix3f mat3) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
		mat3.get(matBuffer); // Upload a buffer to GL
		glUniformMatrix3fv(varLocation, false, matBuffer);
	}
	
	public void uploadVec4f(String varName, Vector4f vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
	}
	
	public void uploadVec3f(String varName, Vector3f vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform3f(varLocation, vec.x, vec.y, vec.z);
	}
	
	public void uploadVec2f(String varName, Vector2f vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform2f(varLocation, vec.x, vec.y);
	}
	
	public void uploadFloat(String varName, float val) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1f(varLocation, val);
	}
	
	public void uploadInt(String varName, int val) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1i(varLocation, val);
	}
	
	public void uploadTexture(String varName, int slot) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1i(varLocation, slot);
	}
	
}
