This file is used to document the process of making a game engine in Java with the tutorial of freeCodeCamp.org:
https://www.youtube.com/watch?v=025QFeZfeyM

This project is about creating my own game engine in Java using Lightweight Java Game Library (LWJGL) with OpenGL. LWJGL allows to use Java binding to native libraries (written in C and C++) like GLFW. It's an interface for Java program to interact with system these system librairies.

The project is using Java 9 (sourceCompatibility = 1.9) while the tutorial uses Java 11 (1.11) from 2018. This is just a test and the final project will be upgraded with Java 21 (2023) later. The lwjgl version is 3.2.3 and the IDE used is Eclipse 4.9 from 2018 (tutorial use Jetbrains' IDEA). SwingDesigner with WindowBuilder for Eclipse 4.9 was installed. The version of Gradle is 6.0 (2019).

The name of the engine is jade.

Classes:
- Main.java: the main class
- Window.java: create window and manage OpenGL context





Core features:
1. The main window is maximized initially and if minimized it will go to 1920x1080.


Notes of style:
1. The name of the package is at the top
2. The import are listed after a space but the static import are separated with another space, after the main imports.
3. @SuppressWarnings("unused") can be used after the import (space too)
4. The Javadoc comment is always just before the class/methods and start with /** and end with */
5. The identation is 4 spaces = 1 tab. Example:

		public class Window {

			/**
			* The window containing...
			* 
			* Contains the method X which do Y.
			*/
			private int width, height;
			...
		}

6. In the constructor the full name of the attribute is required: Window.window for example.