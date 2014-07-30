package timoschwarz.snake.model;

import timoschwarz.snake.controller.GameController;

public interface RuleSet
{
	public void punishSnakeForHittingSnake(Snake snake, GameController controller);

	public void punishSnakeForHittingBounds(Snake snake, GameController controller);

	public void snakeHasConsumedAWorldChanger(Snake snake, GameController controller);

	public void snakeHasConsumedALoosePiece(Snake snake, GameController controller);

	public void snakeHasConsumedABooster(Snake snake, GameController controller);

	public int getPointsForFoodConsumption();

	public int calculatePointsForGrowing(Snake snake);
}
