package timoschwarz.snake.view;

public class Time {

	public static final long NANOSECONDS_PER_SECOND = 1_000_000_000;
	public static final long MILLISECONDS_PER_SECOND = 1_000;
	public static final int NANOSECONDS_PER_MILLISECOND = 1_000_000;

	private long lastTime;
	private long elapsedTime;

	public Time() {
		reset();
	}

	/**
	 * Setzt die Zeit zurück. Die verstrichene Zeit ist somit 0.
	 */
	public void reset() {
		lastTime = System.nanoTime();
		elapsedTime = 0;
	}

	/**
	 * Berechnet die verstrichene Zeit seit dem letzten Update oder reset.
	 */
	public void update() {
		long time = System.nanoTime();
		elapsedTime = time - lastTime;
		lastTime = time;
	}

	/**
	 * Verstrichene Zeit
	 * 
	 * @return verstrichene Zeit
	 */
	public long elapsedTime() {
		return elapsedTime;
	}

	public static double Seconds(long nano) {
		return nano / (double) NANOSECONDS_PER_SECOND;
	}

}
