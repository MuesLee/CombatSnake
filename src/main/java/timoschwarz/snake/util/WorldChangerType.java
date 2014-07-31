package timoschwarz.snake.util;

public enum WorldChangerType {

	GAMESPEEDINCREASE(33), FOODEXPLOSION(67), WORLDSHRINKER(100);

	private int percentage;

	private WorldChangerType(int percentage)
	{
		this.percentage = percentage;
	}

	public static WorldChangerType getWorldChangerByPercentage(int percent)
	{
		WorldChangerType[] values = WorldChangerType.values();
		WorldChangerType current = null;
		for (int i = 0; i < values.length; i++)
		{
			current = values[i];
			if (percent > current.getSpawnPercentage())
			{
				continue;
			}
			else
			{
				return current;
			}
		}

		return WorldChangerType.FOODEXPLOSION;
	}

	public int getSpawnPercentage()
	{
		return percentage;
	}

}
