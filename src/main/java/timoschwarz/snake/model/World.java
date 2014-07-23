package timoschwarz.snake.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.util.BoostFactory;
import timoschwarz.snake.util.BoostType;

public class World
{
	private List<Snake> snakes;
	private GameController controller;
	private LinkedList<Piece> looseSnakePieces;
	private int height;
	private int width;
	private List<Boost> currentBooster;

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
		this.looseSnakePieces = new LinkedList<Piece>();
		this.width = width;
		this.height = height;
		this.setCurrentBooster(new ArrayList<Boost>());
	}

	public void spawnNewBooster()
	{
		BoostType[] values = BoostType.values();
		Random random = new Random();

		int randomIndex = random.nextInt(values.length);
		int x = 0;
		int y = 0;

		do
		{
			x = random.nextInt(width + 1);
			y = random.nextInt(height + 1);
		}
		while (!coordinatesAreFree(x, y));

		BoostType randomType = values[randomIndex];

		Boost boost = BoostFactory.createBooster(randomType, x, y);

		getCurrentBooster().add(boost);
	}

	public void checkForCollisions()
	{
		checkForOutOfBounds();
		checkForSnakeCollisions();
		checkForConsumption();
		checkForBoosterConsumption();
	}

	private void checkForBoosterConsumption()
	{
		for (Snake snake : snakes)
		{
			checkForBoosterConsumptionForSnake(snake);
		}

	}

	private void checkForBoosterConsumptionForSnake(Snake snake)
	{
		int x = snake.getHead().getX();
		int y = snake.getHead().getY();

		Boost usedBooster = null;

		for (Boost booster : getCurrentBooster())
		{
			Piece piece = (Piece) booster;

			if (piece.isAtCoordinates(x, y))
			{
				booster.modifySnake(snake);
				usedBooster = booster;
			}
		}

		if (usedBooster != null)
		{
			currentBooster.remove(usedBooster);
			controller.snakeHasConsumedABooster(usedBooster);
		}
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
			Piece piece = looseSnakePieces.get(i);
			if (x == piece.getX() && y == piece.getY())
			{
				snake.addLooseSnakePieceToConsumeProcess((SnakePiece) piece);
				controller.snakeHasConsumedALoosePiece(snake);
				removeLooseSnakePiece((SnakePiece) piece);
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

		if (looseSnakePieceOrBoosterBlocksCoordinates(randomX, randomY)
			|| snakeOne.snakeBlocksCoordinates(randomX, randomY) || snakeTwo.snakeBlocksCoordinates(randomX, randomY))
		{
			return false;
		}
		return true;
	}

	private boolean looseSnakePieceOrBoosterBlocksCoordinates(int x, int y)
	{
		for (Piece piece : looseSnakePieces)
		{
			if (piece.getX() == x && piece.getY() == y)
			{
				return true;
			}
		}
		for (Boost booster : getCurrentBooster())
		{
			Piece piece = (Piece) booster;

			if (piece.getX() == x && piece.getY() == y)
			{
				return true;
			}
		}

		return false;
	}

	private void checkForSnakeCollisions()
	{
		Snake snakeOne = snakes.get(0);
		Snake snakeTwo = snakes.get(1);

		boolean collisionSnakeOneWithItself;
		boolean collisionSnakeOneWithSnakeTwo;
		boolean collisionSnakeTwoWithItself;
		boolean collisionSnakeTwoWithSnakeOne;

		SnakePiece headOne = snakeOne.getHead();
		SnakePiece headTwo = snakeTwo.getHead();

		if (snakeOne.isPhased())
		{
			collisionSnakeOneWithItself = false;
			collisionSnakeOneWithSnakeTwo = false;
		}

		else
		{
			collisionSnakeOneWithItself = headIntersectsSnake(headOne, snakeOne);
			collisionSnakeOneWithSnakeTwo = headIntersectsSnake(headOne, snakeTwo);

		}
		if (snakeTwo.isPhased())
		{
			collisionSnakeTwoWithItself = false;
			collisionSnakeTwoWithSnakeOne = false;
		}
		else
		{
			collisionSnakeTwoWithItself = headIntersectsSnake(headTwo, snakeTwo);
			collisionSnakeTwoWithSnakeOne = headIntersectsSnake(headTwo, snakeOne);
		}

		if (collisionSnakeOneWithSnakeTwo && collisionSnakeTwoWithSnakeOne)
		{
			controller.endGame(null);
		}
		else if (collisionSnakeOneWithItself || collisionSnakeOneWithSnakeTwo)
		{
			controller.endGame(snakes.get(1));
		}
		else if (collisionSnakeTwoWithItself || collisionSnakeTwoWithSnakeOne)
		{
			controller.endGame(snakes.get(0));
		}

	}

	private boolean headIntersectsSnake(SnakePiece head, Snake snake)
	{
		int xHead = head.getX();
		int yHead = head.getY();

		LinkedList<SnakePiece> pieces = snake.getSnakePieces();

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

	public LinkedList<Piece> getLooseSnakePieces()
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

	public int getAmountOfCurrentBooster()
	{
		return getCurrentBooster().size();
	}

	public List<Boost> getCurrentBooster()
	{
		return currentBooster;
	}

	public void setCurrentBooster(List<Boost> currentBooster)
	{
		this.currentBooster = currentBooster;
	}

}
