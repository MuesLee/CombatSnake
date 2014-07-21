package timoschwarz.snake.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import timoschwarz.snake.controller.Controller;

public class World
{
	private List<Snake> snakes;
	private Controller controller;
	private List<SnakePiece> looseSnakePieces;

	public World(List<Snake> snakes, Controller controller)
	{
		this.controller = controller;
		this.snakes = snakes;
		this.looseSnakePieces = new ArrayList<>();
		this.looseSnakePieces = Collections.synchronizedList(looseSnakePieces);
	}

	public void checkForCollisions()
	{
		checkForOutOfBounds();
		checkForSnakeCollisions();
		checkForConsumption();
	}

	private void checkForConsumption()
	{
		for (int i = 0; i < snakes.size(); i++)
		{
			checkForConsumptionForSnake(snakes.get(i));
		}
	}

	private synchronized void checkForConsumptionForSnake(Snake snake)
	{
		int x = snake.getHead().getX();
		int y = snake.getHead().getY();

		for (int i = 0; i < looseSnakePieces.size(); i++)
		{
			SnakePiece piece = looseSnakePieces.get(i);
			if (x == piece.getX() && y == piece.getY())
			{
				snake.addLooseSnakePieceToConsumeProcess(piece);
				controller.snakeHasConsumedALoosePiece(snake);

			}
		}
	}

	private void checkForOutOfBounds()
	{
		Snake snakeOne = snakes.get(0);
		Snake snakeTwo = snakes.get(1);

		boolean snakeOneIsOutOfBounds = checkForOutOfBoundsForSnake(snakeOne);
		boolean snakeTwoIsOutOfBounds = checkForOutOfBoundsForSnake(snakeTwo);

		if (snakeOneIsOutOfBounds)
		{
			controller.endGame(snakeTwo);
		}
		else if (snakeTwoIsOutOfBounds)
		{
			controller.endGame(snakeOne);
		}

	}

	private boolean checkForOutOfBoundsForSnake(Snake snake)
	{
		int x = snake.getHead().getX();
		int y = snake.getHead().getY();

		if (x > Controller.PLAYGROUND_SIZE_X || x < 0 || y < 0 || y > Controller.PLAYGROUND_SIZE_Y)
		{
			return true;
		}

		return false;
	}

	private void checkForSnakeCollisions()
	{
		Snake snakeOne = snakes.get(0);
		Snake snakeTwo = snakes.get(1);
		SnakePiece headOne = snakeOne.getHead();
		SnakePiece headTwo = snakeOne.getHead();

		boolean collisionSnakeOneWithItself = headIntersectsSnake(headOne, snakeOne);
		boolean collisionSnakeTwoWithItself = headIntersectsSnake(headTwo, snakeTwo);
		boolean collisionSnakeOneWithSnakeTwo = headIntersectsSnake(headOne, snakeTwo);
		boolean collisionSnakeTwoWithSnakeOne = headIntersectsSnake(headTwo, snakeOne);

		if (collisionSnakeOneWithItself && collisionSnakeTwoWithItself)
		{
			System.out.println(Controller.TEXT_BOTH_SNAKES_DED);
			controller.endGame(null);
		}
		else if (collisionSnakeOneWithItself || collisionSnakeOneWithSnakeTwo)
		{
			System.out.println(Controller.TEXT_SNAKE_TWO_WAS_VICTORIOUS);
			controller.endGame(snakes.get(1));
		}
		else if (collisionSnakeTwoWithItself || collisionSnakeTwoWithSnakeOne)
		{
			System.out.println(Controller.TEXT_SNAKE_ONE_WAS_VICTORIOUS);
			controller.endGame(snakes.get(0));
		}
	}

	private boolean headIntersectsSnake(SnakePiece head, Snake snake)
	{
		int xHead = head.getX();
		int yHead = head.getY();

		LinkedList<SnakePiece> pieces = snake.getPieces();

		for (SnakePiece piece : pieces)
		{
			if (piece == head)
			{
				continue;
			}

			int xPiece = piece.getX();
			int yPiece = piece.getY();

			if (xHead == xPiece && yHead == yPiece)
			{
				return true;
			}

		}

		return false;
	}

	public Controller getController()
	{
		return controller;
	}

	public void setController(Controller controller)
	{
		this.controller = controller;
	}

	public List<SnakePiece> getLooseSnakePieces()
	{
		return looseSnakePieces;
	}

	public synchronized void addLooseSnakePiece(SnakePiece piece)
	{
		looseSnakePieces.add(piece);

	}

	public synchronized void removeLooseSnakePiece(SnakePiece piece)
	{
		looseSnakePieces.remove(piece);
	}

}
