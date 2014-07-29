package timoschwarz.snake.view;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Eine auf Frames per second aufbauender Impulsgeber. Wird genutzt um die
 * Szenen in regelmäßigen Abständen neu zu zeichnen.
 * 
 * @author Marvin Bruns
 *
 */
public class Clock extends Thread {

	public static final int FPS_AS_FAST_AS_POSSIBLE = -1;
	public static final int FPS_STOP = 0;

	private volatile double framesPerSecond;
	private volatile boolean running;

	private long lastTime;

	private List<ClockListener> listeners;

	public Clock() {
		this(-1);
	}

	public Clock(float framesPerSecond) {
		this.framesPerSecond = framesPerSecond;
		this.listeners = new LinkedList<ClockListener>();
	}

	public void run() {
		running = true;
		setActualTime();

		while (running) {
			doWork();
		}

	}

	private void setActualTime() {
		lastTime = System.nanoTime();
	}

	private void doWork() {
		double fps = getFramesPerSecond();

		if (fps < FPS_STOP) {
			tick();
			return;
		} else if (fps == FPS_STOP) {
			return;
		}

		long time = System.nanoTime();
		long elapsedTime = time - lastTime;
		int timeForOneFrame = (int) (Time.NANOSECONDS_PER_SECOND / fps);

		if (elapsedTime >= timeForOneFrame) {
			long ticks = elapsedTime / timeForOneFrame;

			for (int i = 0; i < ticks && i >= 0; i++) {
				tick();
			}

			lastTime += ticks * timeForOneFrame;
		} else {
			long timeToTick = timeForOneFrame - elapsedTime;

			try {
				Thread.sleep(timeToTick / Time.NANOSECONDS_PER_MILLISECOND,
						(int) (timeToTick % Time.NANOSECONDS_PER_MILLISECOND));
			} catch (InterruptedException e) {
			}
		}
	}

	private void tick() {
		synchronized (listeners) {
			for (ClockListener cl : listeners) {
				try {
					cl.tick();
				} catch (Exception e) {
				}
			}
		}
	}

	public void addClockListener(ClockListener cl) {
		synchronized (listeners) {
			listeners.add(cl);
		}
	}

	public void removeClockListener(ClockListener cl) {
		synchronized (listeners) {
			listeners.remove(cl);
		}
	}

	public synchronized double getFramesPerSecond() {
		return framesPerSecond;
	}

	public synchronized void setFramesPerSecond(double framesPerSecond) {
		setActualTime();

		this.framesPerSecond = framesPerSecond;
	}

	public void interrupt() {
		running = false;

		try {
			super.interrupt();
		} catch (Exception e) {
		}
	}

}
