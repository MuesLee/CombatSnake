package timoschwarz.snake.model;

import java.awt.Color;
import java.awt.Image;

public class FoodExplosion extends Piece implements WorldChanger
{

	public FoodExplosion(int x, int y)
	{
		super(x, y);
	}

	@Override
	public void modifyWorld(World world)
	{

		for (int i = 0; i < 15; i++)
		{
			world.createNewLooseSnakePiece();
		}
	}

	@Override
	public void restoreWorld(World world)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Color getColor()
	{
		return Color.PINK;
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
		return "worldChanger_FoodExplosion";
	}

}
