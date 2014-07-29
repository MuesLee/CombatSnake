package timoschwarz.snake.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Timer;

import timoschwarz.snake.controller.GameController;

/**
 * 
 * Eine auf Frames per second aufbauender Impulsgeber. Wird genutzt um die Szenen in regelmäßigen
 * Abständen neu zu zeichnen.
 * 
 * @author Marvin Bruns
 * 
 */
public class Clock extends Thread
{

	public static final int FPS_AS_FAST_AS_POSSIBLE = -1;
	public static final int FPS_STOP = 0;

	private volatile double framesPerSecond;
	private volatile boolean running;

	private volatile int realFPS = 0;

	private long lastTime;

	private List<ClockListener> listeners;

	public Clock()
	{
		this(-1);
	}

	public Clock(float framesPerSecond)
	{
		this.framesPerSecond = framesPerSecond;
		this.listeners = new LinkedList<ClockListener>();
	}

	@Override
	public void run()
	{
		running = true;
		setActualTime();

		Timer timer = new Timer((int) Time.MILLISECONDS_PER_SECOND, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				GameController.REAL_FPS = realFPS;
				realFPS = 0;
			}
		});

		timer.start();

		while (running)
		{
			doWork();
		}

	}

	private void setActualTime()
	{
		lastTime = System.nanoTime();
	}

	private void doWork()
	{
		double fps = getFramesPerSecond();

		if (fps < FPS_STOP)
		{
			tick();
			realFPS++;
			return;
		}
		else if (fps == FPS_STOP)
		{
			return;
		}

		long time = System.nanoTime();
		long elapsedTime = time - lastTime;
		int timeForOneFrame = (int) (Time.NANOSECONDS_PER_SECOND / fps);

		if (elapsedTime >= timeForOneFrame)
		{
			long ticks = elapsedTime / timeForOneFrame;

			for (int i = 0; i < ticks && i >= 0; i++)
			{
				tick();
				realFPS++;
			}

			lastTime += ticks * timeForOneFrame;
		}
		else
		{
			long timeToTick = timeForOneFrame - elapsedTime;

			try
			{
				Thread.sleep(timeToTick / Time.NANOSECONDS_PER_MILLISECOND,
					(int) (timeToTick % Time.NANOSECONDS_PER_MILLISECOND));
			}
			catch (InterruptedException e)
			{
			}
		}
	}

	private void tick()
	{
		synchronized (listeners)
		{
			for (ClockListener cl : listeners)
			{
				try
				{
					cl.tick();
				}
				catch (Exception e)
				{
				}
			}
		}
	}

	public void addClockListener(ClockListener cl)
	{
		synchronized (listeners)
		{
			listeners.add(cl);
		}
	}

	public void removeClockListener(ClockListener cl)
	{
		synchronized (listeners)
		{
			listeners.remove(cl);
		}
	}

	public synchronized double getFramesPerSecond()
	{
		return framesPerSecond;
	}

	public synchronized void setFramesPerSecond(double framesPerSecond)
	{
		setActualTime();

		this.framesPerSecond = framesPerSecond;
	}

	@Override
	public void interrupt()
	{
		running = false;

		try
		{
			super.interrupt();
		}
		catch (Exception e)
		{
		}
	}

	public int getRealFPS()
	{
		return realFPS;
	}

	public void setRealFPS(int realFPS)
	{
		this.realFPS = realFPS;
	}

}
