package timoschwarz.snake.controller;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;

import timoschwarz.snake.model.Snake;
import timoschwarz.snake.model.SnakePiece;
import timoschwarz.snake.view.Playground;

public class Referee extends Observable implements Runnable
{

	private Playground playground;
	private LinkedList<Snake> snakes = new LinkedList<Snake>();
	private boolean isActive = true;

	public Referee(Playground playground, LinkedList<Snake> snakes)
	{
		this.setPlayground(playground);
		this.setSnakes(snakes);
	}

	private void checkSnakes()
	{
		Iterator<Snake> iterator = snakes.iterator();

		while (iterator.hasNext())
		{
			checkSnakeMovement(iterator.next());
		}
	}

	private void checkSnakeMovement(Snake snake)
	{
		SnakePiece head = snake.getHead();

		if (pieceIsOutOfBounds(head))
		{
			System.out.println("OUTOFBOUNDS!");
			endGame(snake);
		}
		else if (pieceCollidesWithOtherSnakePiece(head))
		{
			endGame(snake);
		}
	}

	private void endGame(Snake loser)
	{
		setChanged();
		notifyObservers(loser);
	}

	private boolean pieceCollidesWithOtherSnakePiece(SnakePiece headWithNextPosition)
	{

		return false;
	}

	private boolean headIsHittingALooseSnakePiece(SnakePiece headWithNextPosition)
	{
		return false;
	}

	private boolean pieceIsOutOfBounds(SnakePiece head)
	{
		int x = head.getX();
		int y = head.getY();
		Dimension size = getPlayground().getSize();

		return x < 0 || y < 0 || x > size.width || y > size.height;
	}

	public LinkedList<Snake> getSnakes()
	{
		return snakes;
	}

	public void setSnakes(LinkedList<Snake> snakes)
	{
		this.snakes = snakes;
	}

	public Playground getPlayground()
	{
		return playground;
	}

	public void setPlayground(Playground playground)
	{
		this.playground = playground;
	}

	public void run()
	{
		while (isActive())
		{
			try
			{
				Thread.sleep(100);
				checkSnakes();
			}
			catch (InterruptedException e)
			{
			}
		}

	}

	public boolean isActive()
	{
		return isActive;
	}

	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}
}
