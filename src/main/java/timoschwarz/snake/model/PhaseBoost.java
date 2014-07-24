package timoschwarz.snake.model;

import java.awt.Color;
import java.awt.Image;
import java.util.Timer;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.util.SnakeTask;

public class PhaseBoost extends Piece implements Boost
{

	public PhaseBoost(int x, int y)
	{
		super(x, y);
	}

	@Override
	public void modifySnake(final Snake snake)
	{
		snake.setPhased(true);

		Timer timer = new Timer();

		timer.schedule(new SnakeTask(snake, this, timer), GameController.DURATION_PHASEBOOSTER);
	}

	@Override
	public Color getColor()
	{
		return Color.YELLOW;
	}

	@Override
	public Image getImage()
	{
		return null;
	}

	@Override
	public void restoreSnake(Snake snake)
	{
		snake.setPhased(false);
	}

	@Override
	public String toString()
	{
		return "PHASEBOOST";
	}

	@Override
	public String getSoundFileName()
	{
		return "boost_phase";
	}

}
