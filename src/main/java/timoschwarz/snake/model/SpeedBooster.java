package timoschwarz.snake.model;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import timoschwarz.snake.controller.GameController;

public class SpeedBooster extends Piece implements Boost
{

	private int oldSnakeSpeed;

	public SpeedBooster(int x, int y)
	{
		super(x, y);
	}

	@Override
	public void modifySnake(final Snake snake)
	{
		final int speed = snake.getMovementSpeed();
		snake.setMovementSpeed(speed * 2);

		Timer timer = new Timer(GameController.DURATION_SPEEDBOOSTER, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				snake.setMovementSpeed(speed);
			}
		});
		timer.start();
		oldSnakeSpeed = speed;
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
		snake.setMovementSpeed(oldSnakeSpeed);

	}

	@Override
	public String toString()
	{
		return "SPEEDBOOSTER";
	}

	@Override
	public String getSoundFileName()
	{
		return "boost_speed";
	}
}
