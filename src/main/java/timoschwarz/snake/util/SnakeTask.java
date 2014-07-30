package timoschwarz.snake.util;

import java.util.Timer;
import java.util.TimerTask;

import timoschwarz.snake.model.Snake;
import timoschwarz.snake.model.boosts.Boost;

public class SnakeTask extends TimerTask
{

	private Snake snake;
	private Boost booster;
	private Timer timer;

	public SnakeTask(Snake snake, Boost booster, Timer timer)
	{
		this.snake = snake;
		this.booster = booster;
		this.timer = timer;
	}

	@Override
	public void run()
	{
		booster.restoreSnake(snake);
		timer.cancel();
		cancel();
	}
}
