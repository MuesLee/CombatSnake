package timoschwarz.snake.model.powerups;

import java.awt.Color;
import java.awt.Image;

import timoschwarz.snake.model.Piece;
import timoschwarz.snake.model.Snake;

public class GrowBoost extends Piece implements Boost
{

	public GrowBoost(int x, int y)
	{
		super(x, y);
	}

	@Override
	public void modifySnake(Snake snake)
	{
		snake.setGrowSize(snake.getGrowSize() + 1);
	}

	@Override
	public Color getColor()
	{
		return Color.YELLOW;
	}

	@Override
	public Image getImage()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void restoreSnake(Snake snake)
	{
		// THERES NO WAY BACK!!

	}

	@Override
	public String getSoundFileName()
	{
		// TODO Auto-generated method stub
		return "boost_grow";
	}

}
