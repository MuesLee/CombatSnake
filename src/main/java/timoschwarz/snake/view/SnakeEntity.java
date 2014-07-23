package timoschwarz.snake.view;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.model.Snake;
import timoschwarz.snake.model.SnakePiece;

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

	public void checkForNewSnakePieces()
	{
		LinkedList<SnakePiece> pieces = snake.getSnakePieces();
		int paintSize = GameController.paintSize;
		final LinkedList<Double> rects = getRects();

		if (pieces.size() != rects.size())
		{
			SnakePiece last = pieces.getLast();

			final Rectangle2D.Double rect = new Rectangle2D.Double(last.getX() * paintSize
				+ Playground.BORDER_THICKNESS, last.getY() * paintSize + Playground.BORDER_THICKNESS, getCurrentImage()
				.getWidth(), getCurrentImage().getHeight());
			rects.add(rect);

		}
	}

}