package timoschwarz.snake.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.model.Coordinates;

public class Lightning
{
	private List<LightningSegment> segments;
	private int offsetX = 50;
	private int offsetY = 5;
	private Random random = new Random();
	private int generation = 0;

	public Lightning(Coordinates start, Coordinates end)
	{
		segments = new ArrayList<LightningSegment>();
		segments.add(new LightningSegment(start, end, true));
	}

	public List<LightningSegment> getSegments()
	{
		return segments;
	}

	public void setSegments(List<LightningSegment> segments)
	{
		this.segments = segments;
	}

	private double getRandomAngle()
	{
		double randomAngle = random.nextDouble() + 1;

		if (random.nextBoolean())
		{
			return randomAngle + 180;
		}
		return randomAngle;
	}

	public boolean isDone()
	{
		return GameController.MAX_LIGHTNING_GENERATIONS <= generation;
	}

	public void nextGeneration()
	{
		List<LightningSegment> newSegments = new ArrayList<LightningSegment>();

		int size = segments.size();
		for (int i = 0; i < size; i++)
		{
			LightningSegment lightningSegment = segments.get(i);

			if (getGeneration() == GameController.MAX_LIGHTNING_GENERATIONS)
			{
				segments.remove(i);
				size--;

				if (segments.isEmpty())
				{
					return;
				}
				continue;
			}

			Coordinates mid = lightningSegment.getMidCoordsWithOffset(getRandomOffset(offsetX),
				getRandomOffset(offsetY));
			Coordinates start = lightningSegment.getStart();
			Coordinates end = lightningSegment.getEnd();
			LightningSegment startToMid = new LightningSegment(start, mid, lightningSegment.isTrunk());
			LightningSegment MidToEnd = new LightningSegment(mid, end, lightningSegment.isTrunk());

			newSegments.add(startToMid);
			newSegments.add(MidToEnd);

			if (size == 1)
			{
				double angle = getRandomAngle();
				LightningSegment forkEndSeg = getFork(angle, mid, start, end);
				newSegments.add(forkEndSeg);
			}
			else if (size / 2 <= i)
			{
				if (random.nextInt(100) > 50)
				{
					double angle = getRandomAngle();
					LightningSegment forkEndSeg = getFork(angle, mid, start, end);
					newSegments.add(forkEndSeg);
				}
			}
			generation++;

		}

		if (offsetX != 0)
		{
			offsetX /= 2;
		}
		if (offsetY != 0)
		{
			offsetY /= 2;
		}
		segments = newSegments;
	}

	private LightningSegment getFork(double angle, Coordinates mid, Coordinates start, Coordinates end)
	{
		int length = (int) Math.pow(Math.pow(start.getX() - end.getX(), 2) + Math.pow(start.getX() - end.getY(), 2),
			0.5);
		length *= 0.6;
		int xChange = (int) (length * Math.cos(Math.toRadians(angle)));
		int yChange = (int) (length * Math.sin(Math.toRadians(angle)));

		Coordinates forkEnd = new Coordinates(end.getX() + xChange, end.getY() + yChange);
		LightningSegment forkEndSeg = new LightningSegment(mid, forkEnd, false);
		return forkEndSeg;
	}

	private int getRandomOffset(int offset)
	{
		if (offset == 0)
		{
			return 0;
		}

		int nextInt = random.nextInt(offset);

		if (random.nextBoolean())
		{
			nextInt *= -1;
		}

		return nextInt;
	}

	public int getGeneration()
	{
		return generation;
	}

	public void setGeneration(int generation)
	{
		this.generation = generation;
	}
}
