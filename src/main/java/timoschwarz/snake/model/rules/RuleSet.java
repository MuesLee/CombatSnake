package timoschwarz.snake.model.rules;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.model.Player;
import timoschwarz.snake.model.Snake;

public interface RuleSet
{
	public void processSnakeHittingSnake(Snake snake, GameController controller);

	public void processSnakeHittingBounds(Snake snake, GameController controller);

	public void snakeHasConsumedAWorldChanger(Snake snake, GameController controller);

	public void snakeHasConsumedALoosePiece(Snake snake, GameController controller);

	public void snakeHasConsumedABooster(Snake snake, GameController controller);

	public int getPointsForFoodConsumption();

	public int calculatePointsForGrowing(Snake snake);

	public void processFailureOfSnake(Player player, GameController controller);

	public int getPlayerLifes();
}
