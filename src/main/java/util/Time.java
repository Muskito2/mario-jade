package util;

public class Time {
	public static long timeStarted = System.nanoTime();
	
	public static void getTimeStarted( ) {
		System.out.println("timeStarted is initially at" + timeStarted);
	}
	// return in second
	public static double getTime() { return (double) ((System.nanoTime() - timeStarted) * 1E-9); }
	
}
