package timoschwarz.snake.model;

import java.util.LinkedList;

import timoschwarz.util.Diff;
import timoschwarz.util.Direction;

public class Snake
{

	private Direction direction = Direction.RIGHT;
	private LinkedList<SnakePiece> pieces;
	private LinkedList<SnakePiece> consumedLoosePieces;
	private boolean hasMovedAfterLastDirectionChange = false;

	public Snake(int size, int startX, int startY)
	{
		this.consumedLoosePieces = new LinkedList<SnakePiece>();
		initSnake(startX, startY, size);
	}

	private void initSnake(int startX, int startY, int size)
	{
		int x = startX;
		int y = startY;

		pieces = new LinkedList<SnakePiece>();

		for (int i = 0; i < size; i++)
		{
			x = size - i;

			if (i == 0)
			{
				getPieces().add(new SnakePiece(x, y, SnakePieceType.HEAD));
			}
			else if (i == size - 1)
			{
				getPieces().add(new SnakePiece(x, y, SnakePieceType.TAIL));
			}
			else
			{
				getPieces().add(new SnakePiece(x, y, SnakePieceType.BODY));
			}
		}
	}

	public void move()
	{
		SnakePiece tail = getTail();
		moveSnakePieces(createHeadWithNextPosition(direction));
		if (!consumedLoosePieces.isEmpty())
		{
			consumeLooseSnakePieces(tail);
		}
		hasMovedAfterLastDirectionChange = true;
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

		for (int i = 0; i < pieces.size(); i++)
		{
			if (i == 0)
			{
				previousSnakePiece = pieces.get(i).clone();
				moveSnakePieceToPositionOfGivenSnakePiece(i, headWithNextPosition);
			}
			else
			{
				SnakePiece temp = pieces.get(i).clone();
				moveSnakePieceToPositionOfGivenSnakePiece(i, previousSnakePiece);
				previousSnakePiece = temp;
			}
		}
	}

	private void moveSnakePieceToPositionOfGivenSnakePiece(int indexOfMovingSnakePiece, SnakePiece givenSnakePiece)
	{
		SnakePiece currentSnakePiece = getPieces().get(indexOfMovingSnakePiece);
		currentSnakePiece.setX(givenSnakePiece.getX());
		currentSnakePiece.setY(givenSnakePiece.getY());
	}

	public SnakePiece createHeadWithNextPosition(Direction direction)
	{
		if (direction == null)
		{
			return null;
		}

		SnakePiece currentHead = getPieces().get(0);
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
		return pieces.size();
	}

	public void addTail(int x, int y)
	{
		pieces.getLast().setType(SnakePieceType.BODY);
		pieces.add(new SnakePiece(x, y, SnakePieceType.TAIL));
		System.out.println("Snake has GROWN!" + pieces.size());
	}

	public LinkedList<SnakePiece> getPieces()
	{
		return pieces;
	}

	public void setPieces(LinkedList<SnakePiece> pieces)
	{
		this.pieces = pieces;
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
		return pieces.getFirst();
	}

	public SnakePiece getTail()
	{
		return pieces.getLast();
	}

	public boolean snakeBlocksCoordinates(int x, int y)
	{
		for (SnakePiece piece : pieces)
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
		direction = direction;
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

		for (SnakePiece piece : pieces)
		{
			sb.append(piece);
		}

		return sb.toString();
	}
}
