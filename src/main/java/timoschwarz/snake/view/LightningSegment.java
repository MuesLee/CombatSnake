package timoschwarz.snake.view;

import timoschwarz.snake.model.Coordinates;

public class LightningSegment
{

	private Coordinates start;
	private Coordinates end;
	private boolean trunk;

	public LightningSegment(Coordinates start, Coordinates end, boolean trunk)
	{
		this.setTrunk(trunk);
		this.setStart(start);
		this.setEnd(end);
	}

	public Coordinates getStart()
	{
		return start;
	}

	public void setStart(Coordinates start)
	{
		this.start = start;
	}

	public Coordinates getEnd()
	{
		return end;
	}

	public void setEnd(Coordinates end)
	{
		this.end = end;
	}

	public Coordinates getMidCoords()
	{
		int x = (start.getX() + end.getX()) / 2;
		int y = (start.getY() + end.getY()) / 2;

		return new Coordinates(x, y);
	}

	@Override
	public String toString()
	{
		return "TRUNK: " + trunk + " START: + " + start + "END: " + end;
	}

	public Coordinates getMidCoordsWithOffset(int offsetX, int offsetY)
	{
		int sumX = start.getX() + end.getX();
		int sumY = start.getY() + end.getY();

		if (sumX <= 0)
		{
			sumX = 1;
		}
		else if (sumY <= 0)
		{
			sumY = 1;
		}

		int x = sumX / 2 + offsetX;
		int y = sumY / 2 + offsetY;

		return new Coordinates(x, y);
	}

	public boolean isTrunk()
	{
		return trunk;
	}

	public void setTrunk(boolean trunk)
	{
		this.trunk = trunk;
	}
}
