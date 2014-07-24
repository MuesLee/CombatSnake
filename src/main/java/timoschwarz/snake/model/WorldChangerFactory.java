package timoschwarz.snake.model;

import timoschwarz.snake.util.WorldChangerType;

public class WorldChangerFactory
{

	public static WorldChanger createWorldChanger(WorldChangerType type, int x, int y)
	{

		switch (type)
		{
			case FOODEXPLOSION:
				return createFoodExplosion(x, y);
			default:
			break;
		}
		return null;
	}

	private static WorldChanger createFoodExplosion(int x, int y)
	{
		return new FoodExplosion(x, y);
	}

}
