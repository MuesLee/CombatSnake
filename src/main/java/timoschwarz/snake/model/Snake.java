package timoschwarz.snake.model;

import java.util.LinkedList;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.util.Diff;
import timoschwarz.snake.util.Direction;

public class Snake
{

	private Direction direction = Direction.RIGHT;
	private LinkedList<SnakePiece> snakePieces;
	private LinkedList<SnakePiece> consumedLoosePieces;
	private boolean hasMovedAfterLastDirectionChange = false;
	private int movementSpeed = 1;
	private boolean phased = false;
	private boolean isConsuming = false;
	private int timesGrown = 0;

	public Snake(int size, int startX, int startY)
	{
		this.consumedLoosePieces = new LinkedList<SnakePiece>();
		initSnake(startX, startY, size);
	}

	private void initSnake(int startX, int startY, int size)
	{
		int x = startX;
		int y = startY;

		snakePieces = new LinkedList<SnakePiece>();

		for (int i = 0; i < size; i++)
		{
			x = size - i;

			if (i == 0)
			{
				getSnakePieces().add(new SnakePiece(x, y, SnakePieceType.HEAD));
			}
			else if (i == size - 1)
			{
				getSnakePieces().add(new SnakePiece(x, y, SnakePieceType.TAIL));
			}
			else
			{
				getSnakePieces().add(new SnakePiece(x, y, SnakePieceType.BODY));
			}
		}
	}

	public void move(int i)
	{
		if (i >= movementSpeed)
		{
			return;
		}

		SnakePiece tail = getTail();
		moveSnakePieces(createHeadWithNextPosition(direction));

		if (isConsuming && GameController.SNAKE_GROW_SIZE > timesGrown)
		{
			addTail(tail.x, tail.y);
			timesGrown++;

			if (GameController.SNAKE_GROW_SIZE == timesGrown)
			{
				timesGrown = 0;
				isConsuming = false;
			}
		}

		if (!consumedLoosePieces.isEmpty())
		{
			consumeLooseSnakePieces(tail);
		}
		hasMovedAfterLastDirectionChange = true;
		System.out.println("MY NEW SIZE IS: " + snakePieces.size());
	}

	private void consumeLooseSnakePieces(SnakePiece tail)
	{
		int x = tail.getX();
		int y = tail.getY();

		SnakePiece first = consumedLoosePieces.getFirst();
		final int looseX = first.getX();
		final int looseY = first.getY();
		if (x == looseX && y == looseY)
		{
			isConsuming = true;
			addTail(looseX, looseY);
			consumedLoosePieces.removeFirst();
		}

	}

	public void addLooseSnakePieceToConsumeProcess(SnakePiece loosePiece)
	{
		this.consumedLoosePieces.addLast(loosePiece);
	}

	private void moveSnakePieces(SnakePiece headWithNextPosition)
	{
		if (headWithNextPosition == null)
		{
			return;
		}

		SnakePiece previousSnakePiece = null;

		for (int i = 0; i < snakePieces.size(); i++)
		{
			if (i == 0)
			{
				previousSnakePiece = snakePieces.get(i).clone();
				moveSnakePieceToPositionOfGivenSnakePiece(i, headWithNextPosition);
			}
			else
			{
				SnakePiece temp = snakePieces.get(i).clone();
				moveSnakePieceToPositionOfGivenSnakePiece(i, previousSnakePiece);
				previousSnakePiece = temp;
			}
		}
	}

	private void moveSnakePieceToPositionOfGivenSnakePiece(int indexOfMovingSnakePiece, SnakePiece givenSnakePiece)
	{
		SnakePiece currentSnakePiece = getSnakePieces().get(indexOfMovingSnakePiece);
		currentSnakePiece.setX(givenSnakePiece.getX());
		currentSnakePiece.setY(givenSnakePiece.getY());
	}

	public SnakePiece createHeadWithNextPosition(Direction direction)
	{
		if (direction == null)
		{
			return null;
		}

		SnakePiece currentHead = getSnakePieces().get(0);
		SnakePiece nextHead = currentHead.clone();
		Diff diff = direction.getDifForDirection();

		int x = currentHead.getX() + diff.getDifX();
		int y = currentHead.getY() + diff.getDifY();
		nextHead.setX(x);
		nextHead.setY(y);

		return nextHead;
	}

	public int getSize()
	{
		return snakePieces.size();
	}

	public void addTail(int x, int y)
	{
		snakePieces.getLast().setType(SnakePieceType.BODY);
		snakePieces.add(new SnakePiece(x, y, SnakePieceType.TAIL));
	}

	public LinkedList<SnakePiece> getSnakePieces()
	{
		return snakePieces;
	}

	public void setSnakePieces(LinkedList<SnakePiece> pieces)
	{
		this.snakePieces = pieces;
	}

	public Direction getDirection()
	{
		return direction;
	}

	public void setDirection(Direction direction)
	{
		if (this.direction.isOppositeOf(direction))
		{
			return;
		}
		else
		{
			if (!hasMovedAfterLastDirectionChange)
			{
				return;
			}
			this.direction = direction;
			hasMovedAfterLastDirectionChange = false;
		}
	}

	public SnakePiece getHead()
	{
		return snakePieces.getFirst();
	}

	public SnakePiece getTail()
	{
		return snakePieces.getLast();
	}

	public boolean snakeBlocksCoordinates(int x, int y)
	{
		for (SnakePiece piece : snakePieces)
		{
			if (piece.getX() == x && piece.getY() == y)
			{
				return true;
			}
		}

		return false;
	}

	public void forceDirection(Direction direction)
	{
		this.direction = direction;
	}

	public LinkedList<SnakePiece> getConsumedLoosePieces()
	{
		return consumedLoosePieces;
	}

	public void setConsumedLoosePieces(LinkedList<SnakePiece> consumedLoosePieces)
	{
		this.consumedLoosePieces = consumedLoosePieces;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		for (SnakePiece piece : snakePieces)
		{
			sb.append(piece);
		}

		return sb.toString();
	}

	public int getMovementSpeed()
	{
		return movementSpeed;
	}

	public void setMovementSpeed(int movementSpeed)
	{
		this.movementSpeed = movementSpeed;
	}

	public boolean isPhased()
	{
		return phased;
	}

	public LinkedList<Piece> getPieces()
	{
		LinkedList<Piece> pieces = new LinkedList<Piece>();

		for (SnakePiece snakepiece : snakePieces)
		{
			pieces.add(snakepiece);
		}

		return pieces;
	}

	public void setPhased(boolean phased)
	{
		this.phased = phased;
	}
}
