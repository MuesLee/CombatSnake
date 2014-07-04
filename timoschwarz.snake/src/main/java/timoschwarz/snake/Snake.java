package timoschwarz.snake;

import java.util.LinkedList;
import java.util.List;

public class Snake
{
	private int size;
	private LinkedList<SnakePiece> pieces;

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

	public void move(Direction direction)
	{
		moveSnakePieces(createHeadWithNextPosition(direction));
	}

	private void moveSnakePieces(SnakePiece headWithNextPosition)
	{
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

	public List<SnakePiece> getPieces()
	{
		return pieces;
	}

	public void setPieces(LinkedList<SnakePiece> pieces)
	{
		this.pieces = pieces;
	}

}
