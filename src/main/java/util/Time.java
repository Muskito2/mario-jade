package util;

public class Time {
	public static float timeStarted = System.nanoTime();
	
	public static void getTimeStarted( ) {
		System.out.println("timeStarted is initially at" + timeStarted);
	}
	
	public static float getTime() { return (float) ((System.nanoTime() - timeStarted) * 1E-9); }
	
}
