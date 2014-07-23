package timoschwarz.snake.util;

public enum Direction {
	UP, DOWN, LEFT, RIGHT;

	public Diff getDifForDirection()
	{
		Diff diff = null;

		switch (this)
		{
			case UP:
				diff = createDiff(0, -1);
			break;
			case DOWN:
				diff = createDiff(0, 1);
			break;
			case RIGHT:
				diff = createDiff(1, 0);
			break;
			case LEFT:
				diff = createDiff(-1, 0);
			break;
		}

		return diff;

	}

	private Diff createDiff(int difX, int difY)
	{
		return new Diff(difX, difY);
	}

	public boolean isOppositeOf(Direction direction)
	{
		switch (this)
		{
			case UP:
				if (direction == Direction.DOWN)
				{
					return true;
				}
			break;
			case DOWN:
				if (direction == Direction.UP)
				{
					return true;
				}
			break;
			case LEFT:
				if (direction == Direction.RIGHT)
				{
					return true;
				}
			break;
			case RIGHT:
				if (direction == Direction.LEFT)
				{
					return true;
				}
			break;
		}
		return false;
	}

}
