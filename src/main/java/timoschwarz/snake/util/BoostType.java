package timoschwarz.snake.util;

public enum BoostType {

	SPEEDBOOST(50), PHASEBOOST(75), GROWBOOST(100);

	private int spawnPercentage;

	private BoostType(int spawnPercentage)
	{
		this.spawnPercentage = spawnPercentage;
	}

	public static BoostType getBoostTypeByPercentage(int percent)
	{
		BoostType[] values = BoostType.values();
		BoostType current = null;
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

		return BoostType.SPEEDBOOST;
	}

	public int getSpawnPercentage()
	{
		return spawnPercentage;
	}
}
