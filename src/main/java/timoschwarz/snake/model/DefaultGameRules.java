package timoschwarz.snake.model;

import timoschwarz.snake.controller.GameController;

public class DefaultGameRules implements RuleSet
{
	private static int BOUNCE_FROM_BOUNDS_DISTANCE = 2;
	private static int POINTS_FOR_FOOD_CONSUMPTION = 10;
	private static int POINTS_FOR_BOOSTER_CONSUMPTION = 50;
	private static int POINTS_FOR_WORLDCHANGER_CONSUMPTION = 150;

	@Override
	public void punishSnakeForHittingSnake(Snake snake, GameController controller)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void punishSnakeForHittingBounds(Snake snake, GameController controller)
	{
		controller.getWorld().moveWholeSnakeAgainstCurrentDirection(snake, BOUNCE_FROM_BOUNDS_DISTANCE);
	}

	@Override
	public void snakeHasConsumedAWorldChanger(Snake snake, GameController controller)
	{
		Player player = controller.getPlayerForSnake(snake);
		player.increaseScore(POINTS_FOR_WORLDCHANGER_CONSUMPTION);
	}

	@Override
	public void snakeHasConsumedALoosePiece(Snake snake, GameController controller)
	{
		Player player = controller.getPlayerForSnake(snake);

		player.increaseScore(calculatePointsForGrowing(snake));

		controller.getWorld().createNewLooseSnakePiece();
	}

	@Override
	public void snakeHasConsumedABooster(Snake snake, GameController controller)
	{
		Player player = controller.getPlayerForSnake(snake);
		player.increaseScore(POINTS_FOR_BOOSTER_CONSUMPTION);

	}

	@Override
	public int getPointsForFoodConsumption()
	{
		return POINTS_FOR_FOOD_CONSUMPTION;
	}

	@Override
	public int calculatePointsForGrowing(Snake snake)
	{
		return getPointsForFoodConsumption() * (snake.getGrowSize() + 1);
	}

}
