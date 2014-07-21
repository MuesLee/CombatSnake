package timoschwarz.snake.view;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import timoschwarz.snake.model.Snake;

public class SnakeEntity extends Entity
{

	private Snake snake;

	public SnakeEntity(ArrayList<BufferedImage> images, ArrayList<Long> timings, Snake snake)
	{
		super(images, timings, snake.getPieces());

		this.setSnake(snake);
	}

	public Snake getSnake()
	{
		return snake;
	}

	public void setSnake(Snake snake)
	{
		this.snake = snake;
	}

	@Override
	public void move()
	{
		updateRects(snake.getPieces());
	}

}