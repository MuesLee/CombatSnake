package timoschwarz.snake.model.rules;

import java.util.Random;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.model.Player;
import timoschwarz.snake.model.Snake;

public class DefaultGameRules implements RuleSet
{
	private static final int PUSH_DISTANCE_HITTING_BOUNDS = 2;
	private static int VARIABLE_PENALTY_FOR_FAILURE = 4;
	private static int STATIC_PENALTY_FOR_FAILURE = 4;
	private static int POINTS_FOR_FOOD_CONSUMPTION = 10;
	private static int POINTS_FOR_BOOSTER_CONSUMPTION = 50;
	private static int POINTS_FOR_WORLDCHANGER_CONSUMPTION = 150;
	public static int PLAYERS_LIFES = 3;

	@Override
	public void processSnakeHittingSnake(Snake snake, GameController controller)
	{

	}

	@Override
	public void processSnakeHittingBounds(Snake snake, GameController controller)
	{
		controller.getWorld().moveWholeSnakeAgainstCurrentDirection(snake, PUSH_DISTANCE_HITTING_BOUNDS);
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
		if (player == null)
		{
			Player playerTwo = controller.getPlayerTwo();
			Player playerOne = controller.getPlayerOne();

			applyPenaltyToPlayer(playerOne);

			applyPenaltyToPlayer(playerTwo);

			if (playerOne.getLifesLeft() <= 0 || playerTwo.getLifesLeft() <= 0)
			{
				controller.updatePlayerScoreLabel();
				controller.endGame();
			}
		}
		else
		{
			applyPenaltyToPlayer(player);

			if (player.getLifesLeft() <= 0)
			{
				controller.updatePlayerScoreLabel();
				controller.endGame();
			}
		}

		controller.updatePlayerScoreLabel();
	}

	private void applyPenaltyToPlayer(Player player)
	{
		Random random = new Random();

		int lifesLeft = player.getLifesLeft();
		lifesLeft--;
		player.setLifesLeft(lifesLeft);
		int score = player.getScore() / (random.nextInt(VARIABLE_PENALTY_FOR_FAILURE) + 1) - STATIC_PENALTY_FOR_FAILURE;
		player.setScore(score);
	}

	@Override
	public int getPlayerLifes()
	{
		return PLAYERS_LIFES;
	}

	@Override
	public String toString()
	{
		return "FUN MODE";
	}

}
