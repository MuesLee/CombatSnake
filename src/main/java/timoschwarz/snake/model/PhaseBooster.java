package timoschwarz.snake.model;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import timoschwarz.snake.controller.GameController;

public class PhaseBooster extends Piece implements Booster
{

	public PhaseBooster(int x, int y)
	{
		super(x, y);
	}

	@Override
	public void modifySnake(final Snake snake)
	{
		snake.setPhased(true);

		Timer timer = new Timer(GameController.DURATION_SPEEDBOOSTER, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				snake.setPhased(false);
			}
		});
		timer.start();
	}

	@Override
	public Color getColor()
	{
		return Color.blue;
	}

	@Override
	public Image getImage()
	{
		return null;
	}

}
