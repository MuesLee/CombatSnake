package timoschwarz.snake.util;

import timoschwarz.snake.model.Boost;
import timoschwarz.snake.model.PhaseBoost;
import timoschwarz.snake.model.SpeedBoost;

public class BoostFactory
{

	public static Boost createBooster(BoostType type, int x, int y)
	{
		Boost booster = null;

		switch (type)
		{
			case phaseBooster:
				booster = createPhaseBooster(x, y);
			break;
			case speedBooster:
				booster = createSpeedBooster(x, y);
			break;
			default:
			break;
		}

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
