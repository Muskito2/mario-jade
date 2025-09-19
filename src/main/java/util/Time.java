package util;

/**
 * Utility class to source the actual game time.  {@link System#nanoTime()} is a pseudo random value generated when the Java Virtual Machine.
 * 
 * The time given is in double while it's in float in the tutorial.
 * 
 * @author antoi
 * @version  Dev 1.6 Dirty Flags in Rendering
 *
 */
public class Time {
	public static long timeStarted = System.nanoTime();
	
	public static void getTimeStarted( ) {
		System.out.println("timeStarted is initially at" + timeStarted);
	}
	/**
	 * 
	 * @return double time in second
	 */
	public static double getTime() { return (double) ((System.nanoTime() - timeStarted) * 1E-9); }
	
}
