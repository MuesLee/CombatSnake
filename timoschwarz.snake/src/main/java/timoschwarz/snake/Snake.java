package timoschwarz.snake;

import java.util.LinkedList;
import java.util.List;

public class Snake
{
	private int size;
	private LinkedList<SnakePiece> pieces;
	private SnakePiece head;

	public Snake(int size)
	{
		this.setSize(size);
		initSnake();
	}

	private void initSnake()
	{
		int x = 0;
		int y = 0;

		pieces = new LinkedList<SnakePiece>();

		for (int i = 0; i < size; i++)
		{
			x = size - i;

			if (i == 0)
			{
				head = new SnakePiece(x, y, SnakePieceType.HEAD);
				getPieces().add(head);
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

	public void move(Direction direction)
	{

		SnakePiece nextHead = null;

		switch (direction)
		{
			case UP:
				nextHead = createNextHead(0, -1);
			break;
			case DOWN:
				nextHead = createNextHead(0, 1);
			break;
			case RIGHT:
				nextHead = createNextHead(1, 0);
			break;
			case LEFT:
				nextHead = createNextHead(-1, 0);
			break;
		}

		moveRestOfSnake(nextHead);
	}

	private void moveRestOfSnake(SnakePiece nextHead)
	{
		SnakePiece previousSnakePiece = null;

		for (int i = 0; i < pieces.size(); i++)
		{
			if (i == 0)
			{
				previousSnakePiece = pieces.get(i).clone();
				moveSnakePieceToPositionOfGivenSnakePiece(i, nextHead);
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

	private SnakePiece createNextHead(int difX, int difY)
	{
		SnakePiece nextHead = head.clone();

		int x = head.getX() + difX;
		int y = head.getY() + difY;
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

	public List<SnakePiece> getPieces()
	{
		return pieces;
	}

	public void setPieces(LinkedList<SnakePiece> pieces)
	{
		this.pieces = pieces;
	}

}
