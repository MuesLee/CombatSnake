package timoschwarz.snake.util;

import java.util.TimerTask;

import timoschwarz.snake.model.Boost;
import timoschwarz.snake.model.Snake;

public class SnakeTask extends TimerTask
{

	private Snake snake;
	private Boost booster;

	public SnakeTask(Snake snake, Boost booster)
	{
		this.snake = snake;
		this.booster = booster;
	}

	@Override
	public void run()
	{
		booster.restoreSnake(snake);
	}
}
