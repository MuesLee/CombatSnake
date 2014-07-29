package timoschwarz.snake.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.util.BoostFactory;
import timoschwarz.snake.util.BoostType;
import timoschwarz.snake.util.Diff;
import timoschwarz.snake.util.Direction;
import timoschwarz.snake.util.WorldChangerFactory;
import timoschwarz.snake.util.WorldChangerType;

public class World
{
	private List<Snake> snakes;
	private GameController controller;
	private LinkedList<Piece> looseSnakePieces;
	private int height;
	private int width;
	private List<Boost> currentBooster;
	private List<WorldChanger> worldChangers;

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
		this.setWorldChangers(new ArrayList<WorldChanger>());
		this.width = width;
		this.height = height;
		this.setCurrentBooster(new ArrayList<Boost>());
	}

	public void spawnNewWorldChanger()
	{
		int x = 0;
		int y = 0;

		Random random = new Random();
		do
		{
			x = random.nextInt(width + 1);
			y = random.nextInt(height + 1);
		}
		while (!coordinatesAreFree(x, y));

		WorldChangerType type = WorldChangerType.getWorldChangerByPercentage(random.nextInt(101));
		WorldChanger worldChanger = WorldChangerFactory.createWorldChanger(type, x, y);
		getWorldChangers().add(worldChanger);

	}

	public void spawnNewBooster()
	{
		int x = 0;
		int y = 0;

		Random random = new Random();
		do
		{
			x = random.nextInt(width + 1);
			y = random.nextInt(height + 1);
		}
		while (!coordinatesAreFree(x, y));

		BoostType randomType = BoostType.getBoostTypeByPercentage(random.nextInt(101));

		Boost boost = BoostFactory.createBooster(randomType, x, y);

		getCurrentBooster().add(boost);
	}

	public void checkForCollisions()
	{
		checkForOutOfBounds();
		checkForSnakeCollisions();
		checkForConsumption();
		checkForBoosterConsumption();
		checkForWorldChangerConsumption();
	}

	private void checkForWorldChangerConsumption()
	{
		for (Snake snake : snakes)
		{
			checkForWorldChangerConsumptionForSnake(snake);
		}

	}

	private void checkForWorldChangerConsumptionForSnake(Snake snake)
	{
		int x = snake.getHead().getX();
		int y = snake.getHead().getY();

		WorldChanger usedWorldChanger = null;

		for (WorldChanger worldChanger : getWorldChangers())
		{
			Piece piece = (Piece) worldChanger;

			if (piece.isAtCoordinates(x, y))
			{
				worldChanger.modifyWorld(this);
				usedWorldChanger = worldChanger;
			}
		}

		if (usedWorldChanger != null)
		{
			getWorldChangers().remove(usedWorldChanger);
			controller.snakeHasConsumedAWorldChanger(snake, usedWorldChanger);
		}
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
			controller.snakeHasConsumedABooster(snake, usedBooster);
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
			if (piece.isAtCoordinates(x, y))
			{
				snake.addLooseSnakePieceToConsumeProcess((SnakePiece) piece);
				controller.snakeHasConsumedALoosePiece(snake);
				removeLooseSnakePiece((SnakePiece) piece);
			}
		}
	}

	private void checkForOutOfBounds()
	{
		for (Snake snake : snakes)
		{
			if (checkForOutOfBoundsForSnake(snake))
			{
				controller.punishSnakeForHittingTheBounds(snake);
				controller.processFailureOfSnake(snake);
			}
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
		if (looseSnakePieceOrBoosterBlocksCoordinates(randomX, randomY))
		{
			return false;
		}

		for (Snake snake : snakes)
		{
			if (snake.snakeBlocksCoordinates(randomX, randomY))
			{
				return false;
			}
		}

		return true;
	}

	private boolean looseSnakePieceOrBoosterBlocksCoordinates(int x, int y)
	{
		for (Piece piece : looseSnakePieces)
		{
			if (piece.isAtCoordinates(x, y))
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
		//TODO Refactoring

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
			controller.processFailureOfSnake(null);
		}
		else if (collisionSnakeOneWithItself || collisionSnakeOneWithSnakeTwo)
		{
			controller.processFailureOfSnake(snakeTwo);
			controller.punishSnakeForHittingSnake(snakeTwo);
		}
		else if (collisionSnakeTwoWithItself || collisionSnakeTwoWithSnakeOne)
		{
			controller.processFailureOfSnake(snakeOne);
			controller.punishSnakeForHittingSnake(snakeOne);
		}

	}

	private boolean headIntersectsSnake(SnakePiece head, Snake snake)
	{
		int x = head.getX();
		int y = head.getY();

		LinkedList<SnakePiece> pieces = snake.getSnakePieces();

		for (SnakePiece piece : pieces)
		{
			if (piece == head)
			{
				continue;
			}

			if (piece.isAtCoordinates(x, y))
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

	public int getAmountOfCurrentWorldChanger()
	{
		return getWorldChangers().size();

	}

	public List<WorldChanger> getWorldChangers()
	{
		return worldChangers;
	}

	public void setWorldChangers(List<WorldChanger> worldChangers)
	{
		this.worldChangers = worldChangers;
	}

	public void moveWholeSnakeAgainstCurrentDirection(Snake snake, int units)
	{
		Direction direction = snake.getDirection();
		Direction opposite = null;
		Direction[] values = Direction.values();
		for (int i = 0; i < values.length; i++)
		{
			opposite = values[i];
			if (opposite.isOppositeOf(direction))
			{
				break;
			}
		}
		shiftWholeSnakeIntoDirection(snake, opposite, units);

	}

	public void shiftWholeSnakeIntoDirection(Snake snake, Direction direction, int units)
	{
		Diff difForDirection = direction.getDifForDirection();
		for (Piece piece : snake.getSnakePieces())
		{
			piece.x += difForDirection.getDifX() * units;
			piece.y += difForDirection.getDifY() * units;
		}
	}

}
