package timoschwarz.snake.util;

import timoschwarz.snake.model.Boost;
import timoschwarz.snake.model.GrowBoost;
import timoschwarz.snake.model.PhaseBoost;
import timoschwarz.snake.model.SpeedBoost;

public class BoostFactory
{
	//"FACTORY", he said... 

	public static Boost createBooster(BoostType type, int x, int y)
	{
		Boost booster = null;

		switch (type)
		{
			case PHASEBOOST:
				booster = createPhaseBooster(x, y);
			break;
			case SPEEDBOOST:
				booster = createSpeedBooster(x, y);
			break;
			case GROWBOOST:
				booster = createGrowBooster(x, y);
			default:
			break;
		}

		return booster;

	}

	private static Boost createGrowBooster(int x, int y)
	{
		Boost booster = new GrowBoost(x, y);

		return booster;
	}

	private static Boost createSpeedBooster(int x, int y)
	{
		Boost booster = new SpeedBoost(x, y);

		return booster;
	}

	private static Boost createPhaseBooster(int x, int y)
	{
		Boost booster = new PhaseBoost(x, y);

		return booster;
	}
}
