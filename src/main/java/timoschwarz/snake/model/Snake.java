package timoschwarz.snake.model;

import java.util.LinkedList;
import java.util.Observable;

import timoschwarz.util.Diff;
import timoschwarz.util.Direction;

public class Snake extends Observable implements Runnable
{

	private Direction direction = Direction.RIGHT;
	private int size;
	private LinkedList<SnakePiece> pieces;
	private boolean isAlive = true;
	private int millis = 300;

	public Snake(int size, int startX, int startY)
	{
		this.setSize(size);
		initSnake(startX, startY);
	}

	private void initSnake(int startX, int startY)
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

	public synchronized void move(Direction direction)
	{
		moveSnakePieces(createHeadWithNextPosition(direction));
		//System.out.println("Head X: " + getHead().getX() + "Y: " + getHead().getY());
	}

	private synchronized void moveSnakePieces(SnakePiece headWithNextPosition)
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

	private synchronized void moveSnakePieceToPositionOfGivenSnakePiece(int indexOfMovingSnakePiece,
		SnakePiece givenSnakePiece)
	{
		SnakePiece currentSnakePiece = getPieces().get(indexOfMovingSnakePiece);
		currentSnakePiece.setX(givenSnakePiece.getX());
		currentSnakePiece.setY(givenSnakePiece.getY());
	}

	public synchronized SnakePiece createHeadWithNextPosition(Direction direction)
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
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public void addTail()
	{
		this.size++;
	}

	public LinkedList<SnakePiece> getPieces()
	{
		return pieces;
	}

	public void setPieces(LinkedList<SnakePiece> pieces)
	{
		this.pieces = pieces;
	}

	@Override
	public void run()
	{
		while (isAlive())
		{
			try
			{
				Thread.sleep(millis);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			move(getDirection());
		}
	}

	public Direction getDirection()
	{
		return direction;
	}

	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}

	public SnakePiece getHead()
	{
		return pieces.getFirst();
	}

	public boolean isAlive()
	{
		return isAlive;
	}

	public void setAlive(boolean isAlive)
	{
		this.isAlive = isAlive;
	}

}
