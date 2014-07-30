package timoschwarz.snake.model;

import java.util.Random;

import timoschwarz.snake.controller.GameController;

public class DefaultGameRules implements RuleSet
{
	private static int BOUNCE_FROM_BOUNDS_DISTANCE = 2;
	private static int POINTS_FOR_FOOD_CONSUMPTION = 10;
	private static int POINTS_FOR_BOOSTER_CONSUMPTION = 50;
	private static int POINTS_FOR_WORLDCHANGER_CONSUMPTION = 150;
	public static int PLAYERS_LIFES = 3;

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

	@Override
	public void processFailureOfSnake(Player player, GameController controller)
	{
		Random random = new Random();

		if (player == null)
		{
			Player playerTwo = controller.getPlayerTwo();
			Player playerOne = controller.getPlayerOne();

			int lifesLeftPlayerTwo = playerTwo.getLifesLeft();
			lifesLeftPlayerTwo--;
			playerTwo.setLifesLeft(lifesLeftPlayerTwo);
			int score = playerTwo.getScore() / (random.nextInt(GameController.PENALTY_FOR_FAILURE) + 1);
			playerTwo.setScore(score);

			score = playerOne.getScore() / (random.nextInt(GameController.PENALTY_FOR_FAILURE) + 1);
			playerOne.setScore(score);
			int lifesLeftPlayerOne = playerOne.getLifesLeft();
			lifesLeftPlayerOne--;
			playerOne.setLifesLeft(lifesLeftPlayerOne);

			if (lifesLeftPlayerTwo == 0 || lifesLeftPlayerOne == 0)
			{
				controller.updatePlayerScoreLabel();
				controller.endGame();
			}
		}
		else
		{
			int lifesLeft = player.getLifesLeft();
			lifesLeft--;
			player.setLifesLeft(lifesLeft);
			int score = player.getScore() / (random.nextInt(GameController.PENALTY_FOR_FAILURE) + 1);
			player.setScore(score);

			if (lifesLeft == 0)
			{
				controller.updatePlayerScoreLabel();
				controller.endGame();
			}
		}

		controller.updatePlayerScoreLabel();

	}
}
