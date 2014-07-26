package timoschwarz.snake.util;

import timoschwarz.snake.model.FoodExplosion;
import timoschwarz.snake.model.GameSpeedIncrease;
import timoschwarz.snake.model.WorldChanger;

public class WorldChangerFactory {

	public static WorldChanger createWorldChanger(WorldChangerType type, int x,
			int y) {

		switch (type) {
		case FOODEXPLOSION:
			return createFoodExplosion(x, y);
		default:
		case GAMESPEEDINCREASE:
			return createGameSpeedBoost(x, y);
		}
	}

	private static WorldChanger createGameSpeedBoost(int x, int y) {
		// TODO Auto-generated method stub
		return new GameSpeedIncrease(x, y);
	}

	private static WorldChanger createFoodExplosion(int x, int y) {
		return new FoodExplosion(x, y);
	}

}
