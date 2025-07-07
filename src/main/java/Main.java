import jade.Window;

/** 
 * Main entry point of the application.
 * 
 * This class contains the {@code main} method which starts the program.
 */
public class Main {

	/**
	 * The main method that gets called when the application starts.
	 * 
	 * @param args command-line arguments passed to the program
	 */
	public static void main(String[] args) {
		Window window = Window.get(); // Window is a singleton
		window.run();
	}

}