package timoschwarz.snake.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import timoschwarz.snake.controller.GameController;

public class World
{
	private List<Snake> snakes;
	private GameController controller;
	private LinkedList<SnakePiece> looseSnakePieces;
	private int height;
	private int width;
	private List<Booster> allPowerUps;
	private List<Booster> currentPowerUps;
	private Timer powerUptimer;

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public World(LinkedList<Snake> snakes, GameController controller, int width, int height)
	{
		this.controller = controller;
		this.snakes = snakes;
		this.looseSnakePieces = new LinkedList<>();
		this.width = width;
		this.height = height;
		this.allPowerUps = new ArrayList<Booster>();
		this.currentPowerUps = new ArrayList<Booster>();
		this.powerUptimer = new Timer();
	}

	private void fillAllPowerUps()
	{
		allPowerUps.add(null);
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

	private void checkForConsumptionForSnake(Snake snake)
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
				removeLooseSnakePiece(piece);
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

		if (x > width || x < 0 || y < 0 || y > height)
		{
			return true;
		}

		return false;
	}

	public void createNewPowerUp()
	{

	}

	public void createNewLooseSnakePiece()
	{
		Random random = new Random();

		int randomX = 0;
		int randomY = 0;
		do
		{
			randomX = random.nextInt(width + 1);
			randomY = random.nextInt(height + 1);
		}
		while (!coordinatesAreFree(randomX, randomY));

		SnakePiece snakePiece = new SnakePiece(randomX, randomY, SnakePieceType.LOOSE);

		addLooseSnakePiece(snakePiece);
	}

	private boolean coordinatesAreFree(int randomX, int randomY)
	{
		Snake snakeOne = snakes.get(0);
		Snake snakeTwo = snakes.get(1);

		if (snakeOne.snakeBlocksCoordinates(randomX, randomY) || snakeTwo.snakeBlocksCoordinates(randomX, randomY))
		{
			return false;
		}
		return true;
	}

	private void checkForSnakeCollisions()
	{
		Snake snakeOne = snakes.get(0);
		Snake snakeTwo = snakes.get(1);
		SnakePiece headOne = snakeOne.getHead();
		SnakePiece headTwo = snakeTwo.getHead();

		boolean collisionSnakeOneWithItself = headIntersectsSnake(headOne, snakeOne);
		boolean collisionSnakeTwoWithItself = headIntersectsSnake(headTwo, snakeTwo);
		boolean collisionSnakeOneWithSnakeTwo = headIntersectsSnake(headOne, snakeTwo);
		boolean collisionSnakeTwoWithSnakeOne = headIntersectsSnake(headTwo, snakeOne);

		if (collisionSnakeOneWithSnakeTwo && collisionSnakeTwoWithSnakeOne)
		{
			System.out.println(GameController.TEXT_BOTH_SNAKES_DEAD);
			controller.endGame(null);
		}
		else if (collisionSnakeOneWithItself || collisionSnakeOneWithSnakeTwo)
		{
			System.out.println(GameController.TEXT_SNAKE_TWO_WAS_VICTORIOUS);
			controller.endGame(snakes.get(1));
		}
		else if (collisionSnakeTwoWithItself || collisionSnakeTwoWithSnakeOne)
		{
			System.out.println(GameController.TEXT_SNAKE_ONE_WAS_VICTORIOUS);
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

	public GameController getController()
	{
		return controller;
	}

	public void setController(GameController controller)
	{
		this.controller = controller;
	}

	public LinkedList<SnakePiece> getLooseSnakePieces()
	{
		return looseSnakePieces;
	}

	public void addLooseSnakePiece(SnakePiece piece)
	{
		looseSnakePieces.add(piece);

	}

	public void removeLooseSnakePiece(SnakePiece piece)
	{
		looseSnakePieces.remove(piece);
	}

}
