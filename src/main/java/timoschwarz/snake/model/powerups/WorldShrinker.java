package timoschwarz.snake.model.powerups;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.model.Piece;
import timoschwarz.snake.model.World;

public class WorldShrinker extends Piece implements WorldChanger
{

	private int oldWidth;
	private int oldHeigth;
	private Timer timer;
	private int shrinkCounter;

	public WorldShrinker(int x, int y)
	{
		super(x, y);
	}

	@Override
	public void modifyWorld(final World world)
	{
		final GameController controller = world.getController();

		oldWidth = world.getWidth();
		oldHeigth = world.getHeight();

		timer = new Timer(GameController.WORLD_SHRINKER_INTERVAL, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (shrinkCounter < GameController.WORLD_SHRINKER_ITERATIONS)
				{
					int width = world.getWidth();
					int height = world.getHeight();

					width = (int) (width * GameController.WORLD_SHRINKER_MULTIPLIER);
					height = (int) (width * GameController.WORLD_SHRINKER_MULTIPLIER);

					world.setWidth(width);
					world.setHeight(height);
					world.revalidatePieces();
					controller.worldSizeHasBeenUpdated();
					shrinkCounter++;
				}
				else
				{
					restoreWorld(world);
				}
			}
		});

		timer.start();

	}

	@Override
	public void restoreWorld(World world)
	{
		final GameController controller = world.getController();

		world.setWidth(oldWidth);
		world.setHeight(oldHeigth);
		controller.worldSizeHasBeenUpdated();
		timer.stop();

	}

	@Override
	public Color getColor()
	{
		// TODO Auto-generated method stub
		return Color.pink;
	}

	@Override
	public Image getImage()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSoundFile()
	{
		// TODO Auto-generated method stub
		return "worldShrinker";
	}

	public int getOldHeigth()
	{
		return oldHeigth;
	}

	public void setOldHeigth(int oldHeigth)
	{
		this.oldHeigth = oldHeigth;
	}

	public int getOldWidth()
	{
		return oldWidth;
	}

	public void setOldWidth(int oldWidth)
	{
		this.oldWidth = oldWidth;
	}

}
