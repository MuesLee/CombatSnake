package timoschwarz.snake.model;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import timoschwarz.snake.controller.GameController;

public class SpeedBooster extends Piece implements Booster
{

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
}
