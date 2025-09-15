package util;

import renderer.Shader;
import renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import components.Spritesheet;

public class AssetPool {
	private static Map<String, Shader> shaders = new HashMap<>();
	private static Map<String, Texture> textures = new HashMap<>();
	private static Map<String, Spritesheet> spritesheets = new HashMap<>();
	
	public static Shader getShader(String ressourceName) {
		File file = new File(ressourceName);
		if (AssetPool.shaders.containsKey(file.getAbsolutePath())) {
			return AssetPool.shaders.get(file.getAbsolutePath());
		} else {
			Shader shader = new Shader(ressourceName);
			shader.compile();
			AssetPool.shaders.put(file.getAbsolutePath(), shader);
			return shader;
		}
	}
	
	public static Texture getTexture(String ressourceName) {
		File file = new File(ressourceName);
		if (AssetPool.textures.containsKey(file.getAbsolutePath())) {
			return AssetPool.textures.get(file.getAbsolutePath());
		} else {
			Texture texture = new Texture(ressourceName);
			AssetPool.textures.put(file.getAbsolutePath(), texture);
			return texture;
		}
	}
	
	public static void addSpritesheet(String resourceName, Spritesheet spritesheet) {
		File file = new File(resourceName); // Do not get it
		if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
			AssetPool.spritesheets.put(file.getAbsolutePath(), spritesheet);
		}
	}
	
	public static Spritesheet getSpritesheet(String resourceName) {
		File file = new File(resourceName);
		if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
			assert false : "Error : Tried to access spritesheet '" + resourceName + "' and it has not been added to the asset pool";
		}
		
		return AssetPool.spritesheets.getOrDefault(file.getAbsolutePath(), null); // CAUTION: getAbsolutePath and not getAbsoluteFIle
	}

}
