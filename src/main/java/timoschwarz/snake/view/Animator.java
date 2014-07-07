package timoschwarz.snake.view;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

class Animator
{

	private ArrayList<BufferedImage> frames;
	private ArrayList<Long> timings;
	private int currIndex;
	private long animationTime;
	private long totalAnimationDuration;
	private AtomicBoolean done;//used to keep track if a single set of frames/ an animtion has finished its loop

	public Animator(ArrayList<BufferedImage> frames, ArrayList<Long> timings)
	{
		currIndex = 0;
		animationTime = 0;
		totalAnimationDuration = 0;
		done = new AtomicBoolean(false);
		this.frames = new ArrayList<BufferedImage>();
		this.timings = new ArrayList<Long>();
		setFrames(frames, timings);
	}

	public boolean isDone()
	{
		return done.get();
	}

	public void reset()
	{
		totalAnimationDuration = 0;
		done.getAndSet(false);
	}

	public void update(long elapsedTime)
	{
		if (frames.size() > 1)
		{
			animationTime += elapsedTime;
			if (animationTime >= totalAnimationDuration)
			{
				animationTime = animationTime % totalAnimationDuration;
				currIndex = 0;
				done.getAndSet(true);
			}
			while (animationTime > timings.get(currIndex))
			{
				currIndex++;
			}
		}
	}

	public BufferedImage getCurrentImage()
	{
		if (frames.isEmpty())
		{
			return null;
		}
		else
		{
			try
			{
				return frames.get(currIndex);
			}
			catch (Exception ex)
			{//images might have been altered so we reset the index and return first image of the new frames/animation 
				currIndex = 0;
				return frames.get(currIndex);
			}
		}
	}

	public void setFrames(ArrayList<BufferedImage> frames, ArrayList<Long> timings)
	{
		if (frames == null || timings == null)
		{//so that constructor super(null,null) cause this to throw NullPointerException
			return;
		}
		this.frames = frames;
		this.timings.clear();
		for (long animTime : timings)
		{
			totalAnimationDuration += animTime;
			this.timings.add(totalAnimationDuration);
		}
	}
}